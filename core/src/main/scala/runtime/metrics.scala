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

import freestyle._
import freestyle.implicits._
import freestyle.fs2.implicits._
import freestyle.opscenter.model.{ListMetrics, Metric}
import freestyle.opscenter.runtime.metrics.implicits._
import _root_.fs2.{time, Scheduler, Strategy, Stream, Task}
import _root_.fs2._
import org.http4s.websocket.WebsocketBits.{Binary, Text}
import org.http4s.websocket.WebsocketBits.WebSocketFrame
import java.time.Instant

import scala.concurrent.duration._
import scala.util.Random

object implicits {

  implicit val scheduler = Scheduler.fromFixedDaemonPool(2)
  implicit val strategy =
    Strategy.fromExecutionContext(scala.concurrent.ExecutionContext.Implicits.global)

  implicit def metricsHandler[M[_]](implicit C: Capture[M]): MetricsM.Handler[M] =
    new MetricsM.Handler[M] {

      private def randomMetrics: Seq[Metric] = {
        val microservices = Seq("analytics", "users", "payments")
        val nodes         = Seq("node-1", "node-2", "node-4")
        val metrics       = Seq("cassandra.queue", "instance.cpu.usage", "instance.cpu.disk")

        for {
          microservice <- microservices
          node         <- nodes
          metric       <- metrics
          value     = Random.nextInt()
          timestamp = Instant.now.getEpochSecond
        } yield Metric(metric, microservice, node, value.toFloat, timestamp)

      }

      def signalFromClient: M[Sink[Task, WebSocketFrame]] = {

        val fromClient: Sink[Task, WebSocketFrame] = _.evalMap { (ws: WebSocketFrame) =>
          ws match {
            case Text(t, _) => Task.delay(println(s"Received from client: $t"))
            case f          => Task.delay(println(s"Received from client unknown type: $f"))
          }
        }
        C.capture(fromClient)
      }

      def streamMetrics: M[Stream[Task, WebSocketFrame]] = {
        val stream: Stream[Task, Binary] = time
          .awakeEvery[Task](1.second)
          .map { d =>
            Binary(ListMetrics(randomMetrics.map(_.toProto)).toProto.toByteArray)
          }
        C.capture(stream)
      }
    }

}
