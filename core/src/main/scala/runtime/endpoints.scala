/*
 * Copyright 2017 47 Degrees, LLC. <http://www.47deg.com>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package freestyle
package opscenter
package runtime
package endpoints

import java.io.File

import freestyle._
import freestyle.implicits._
import org.http4s.dsl.impl.Root
import org.http4s.{HttpService, StaticFile}
import org.http4s.server.websocket.WS
import org.http4s.dsl.io._
import pbdirect._
import _root_.fs2.{Scheduler, Stream}
import freestyle.opscenter.Models.{Metric, MetricsList}
import freestyle.opscenter.Models.Metric.implicits._
import _root_.fs2._
import org.http4s.websocket.WebsocketBits.{Binary, Text}
import org.http4s.websocket.WebsocketBits.WebSocketFrame
import scala.concurrent.ExecutionContext.Implicits.global
import cats.effect.IO
import scala.concurrent.duration._
import org.joda.time.DateTime
import scala.util.Random

object implicits {

  implicit def endpointsHandler[M[_]](implicit C: Capture[M]): Endpoints.Handler[M] =
    new Endpoints.Handler[M] {

      def protoModels: M[HttpService[IO]] =
        C.capture(HttpService[IO] {
          case request @ GET -> Root / "proto" / "models" =>
            StaticFile
              .fromFile(new File("core/src/main/proto/Models.proto"), Some(request))
              .getOrElseF(NotFound())
        })

      def healthcheck: M[HttpService[IO]] =
        C.capture(HttpService[IO] {
          case GET -> Root / "healthcheck" => Ok(s"Works fine.")
        })

      def streamMetrics: M[HttpService[IO]] =
        C.capture(HttpService[IO] {
          case GET -> Root / "metrics" => WS(metricsStream, signalFromClient)
        })

      private def randomMetrics: List[Metric] = {
        val microservices = List("analytics", "users", "payments")
        val nodes         = List("node-1", "node-2", "node-4")
        val metrics       = List("cassandra.queue", "instance.cpu.usage", "instance.cpu.disk")

        for {
          microservice <- microservices
          node         <- nodes
          metric       <- metrics
          value     = Random.nextInt()
          timestamp = DateTime.now()
        } yield Metric(metric, microservice, node, value.toFloat, timestamp)

      }

      private def signalFromClient: Sink[IO, WebSocketFrame] = _.evalMap { (ws: WebSocketFrame) =>
        ws match {
          case Text(t, _) => IO.apply(println(s"Received from client: $t"))
          case f          => IO.apply(println(s"Received from client unknown type: $f"))
        }
      }

      private def metricsStream: Stream[IO, WebSocketFrame] =
        Scheduler[IO](2)
          .flatMap(_.awakeEvery[IO](1.second))
          .map { d =>
            Binary(MetricsList(randomMetrics).toPB)
          }

    }

}
