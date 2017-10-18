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

import freestyle._
import freestyle.config.ConfigM
import _root_.fs2.{Sink, Stream, Task}
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import cats.syntax.semigroup._
import org.http4s.websocket.WebsocketBits.WebSocketFrame

// Modules
@module trait OpscenterApp {
  val http: Http
  val services: Services
}

@module trait Services {
  val config: ConfigM
}

@module trait Http {
  val server: Server
  val endpoints: Endpoints
  val metrics: Metrics

  def buildServer(host: String, port: Int): FS.Seq[BlazeBuilder] = {
    for {
      streamMetrics <- metrics.streamMetrics
      fromClient    <- metrics.signalFromClient
      endpoints     <- endpoints.build(streamMetrics, fromClient)
      server        <- server.getServer(host, port, endpoints)
    } yield server
  }
}

// Algebras
@free trait Server {
  def getServer(host: String, port: Int, endpoints: HttpService): FS[BlazeBuilder]
}

@free trait Endpoints {
  def healthcheck: FS[HttpService]

  def protoMetric: FS[HttpService]

  def websocketMetrics(
      streamMetrics: Stream[Task, WebSocketFrame],
      signalFromClient: Sink[Task, WebSocketFrame]): FS[HttpService]

  def build(
      streamMetrics: Stream[Task, WebSocketFrame],
      fromClient: Sink[Task, WebSocketFrame]): FS.Seq[HttpService] = {
    for {
      healthEndpoint   <- healthcheck
      protoEndpoint    <- protoMetric
      metricsWebsocket <- websocketMetrics(streamMetrics, fromClient)
    } yield healthEndpoint |+| metricsWebsocket |+| protoEndpoint
  }
}

@free trait Metrics {
  def streamMetrics: FS[Stream[Task, WebSocketFrame]]
  def signalFromClient: FS[Sink[Task, WebSocketFrame]]
}
