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
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import org.http4s.server.blaze._
import org.http4s.implicits._
import _root_.fs2.Stream
import cats.effect.IO
import cats.implicits._
import _root_.fs2._
import cats.syntax.semigroup._
import org.http4s.websocket.WebsocketBits.WebSocketFrame
import cats.effect._
import cats.implicits._
import org.http4s._
import org.http4s.implicits._
import org.http4s.dsl.io._

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

  def buildServer(host: String, port: Int): FS.Seq[BlazeBuilder[IO]] = {
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
  def getServer(host: String, port: Int, endpoints: HttpService[IO]): FS[BlazeBuilder[IO]]
}

@free trait Endpoints {
  def healthcheck: FS[HttpService[IO]]

  def protoMetric: FS[HttpService[IO]]

  def websocketMetrics(
      streamMetrics: Stream[IO, WebSocketFrame],
      signalFromClient: Sink[IO, WebSocketFrame]): FS[HttpService[IO]]

  def build(
      streamMetrics: Stream[IO, WebSocketFrame],
      fromClient: Sink[IO, WebSocketFrame]): FS.Seq[HttpService[IO]] =
    (healthcheck, protoMetric, websocketMetrics(streamMetrics, fromClient)).mapN(_ <+> _ <+> _)
}

@free trait Metrics {
  def streamMetrics: FS[Stream[IO, WebSocketFrame]]
  def signalFromClient: FS[Sink[IO, WebSocketFrame]]
}
