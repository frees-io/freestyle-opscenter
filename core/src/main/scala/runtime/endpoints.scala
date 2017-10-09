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

import freestyle._
import freestyle.implicits._
import freestyle.opscenter.runtime.metrics.implicits._
import cats.Applicative
import cats.implicits._
import _root_.fs2._
import org.http4s.{HttpService}
import org.http4s.server.websocket.WS
import org.http4s.dsl.{->, /, GET, Ok, Root, _}
import org.http4s.websocket.WebsocketBits.WebSocketFrame

object implicits {

  import cats.syntax.applicative._

  implicit def endpointsHandler[M[_]: Applicative]: EndpointsM.Handler[M] =
    new EndpointsM.Handler[M] {

      def healthcheck: M[HttpService] =
        HttpService {
          case GET -> Root / "healthcheck" => Ok(s"Works fine.")
        }.pure[M]

      def websocketMetrics(
          metricsStream: Stream[Task, WebSocketFrame],
          signalFromClient: Sink[Task, WebSocketFrame]): M[HttpService] =
        HttpService {
          case GET -> Root / "metrics" => WS(metricsStream, signalFromClient)
        }.pure[M]

    }
}
