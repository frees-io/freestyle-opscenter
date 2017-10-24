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
package metrics

import pbdirect._
import cats.instances.list._
import _root_.fs2.{Scheduler, Stream}
import freestyle.opscenter.Models.{Metric, Metrics, MetricsList}
import _root_.fs2._
import org.http4s.websocket.WebsocketBits.{Binary, Text}
import org.http4s.websocket.WebsocketBits.WebSocketFrame
import java.time.Instant
import scala.concurrent.ExecutionContext.Implicits.global
import cats.effect.IO
import scala.concurrent.duration._
import scala.util.Random

object implicits {

  implicit def metricsHandler[M[_]](implicit C: Capture[M]): Metrics.Handler[M] =
    new Metrics.Handler[M] {

      private def randomMetrics: Metrics = {
        val microservices = List("analytics", "users", "payments")
        val nodes         = List("node-1", "node-2", "node-4")
        val metrics       = List("cassandra.queue", "instance.cpu.usage", "instance.cpu.disk")

        for {
          microservice <- microservices
          node         <- nodes
          metric       <- metrics
          value     = Random.nextInt()
          timestamp = Instant.now.getEpochSecond
        } yield Metric(metric, microservice, node, value.toFloat, timestamp)

      }

      def signalFromClient: M[Sink[IO, WebSocketFrame]] = {
        val fromClient: Sink[IO, WebSocketFrame] = _.evalMap { (ws: WebSocketFrame) =>
          ws match {
            case Text(t, _) => IO.apply(println(s"Received from client: $t"))
            case f          => IO.apply(println(s"Received from client unknown type: $f"))
          }
        }
        C.capture(fromClient)
      }

      def streamMetrics: M[Stream[IO, WebSocketFrame]] = {
        val stream: Stream[IO, Binary] = Scheduler[IO](2)
          .flatMap(_.awakeEvery[IO](1.second))
          .map { d =>
            Binary(MetricsList(randomMetrics).toPB)
          }
        C.capture(stream)
      }
    }

}
