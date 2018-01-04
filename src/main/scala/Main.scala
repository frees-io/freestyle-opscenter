/*
 * Copyright 2017-2018 47 Degrees, LLC. <http://www.47deg.com>
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

import cats.Id
import freestyle.opscenter.runtime.server.implicits._
import freestyle.opscenter.runtime.endpoints.implicits._
import freestyle.metrics.runtime.default.implicits._
import freestyle.free.loggingJVM.implicits._
import freestyle.free._
import freestyle.opscenter._
import freestyle.free.implicits._
import freestyle.free.config.implicits._
import cats.implicits._
import _root_.fs2.Stream
import cats.effect.IO
import freestyle.free.config.ConfigM
import freestyle.free.logging.LoggingM
import freestyle.metrics.Metrics
import org.http4s.server.blaze._
import org.http4s.util._
import fs2.StreamApp.ExitCode

import scala.util.Try

object Main extends StreamApp[IO] {

  def bootstrap[T[_]](implicit app: OpscenterApp[T]): FreeS[T, BlazeBuilder[IO]] = {

    for {
      config <- app.services.config.load
      host = config.string("http.host").getOrElse("localhost")
      port = config.int("http.port").getOrElse(8080)

      // Example of metrics usage
      maxMemory   <- app.metrics.defaultMemory.max
      usedMemory  <- app.metrics.defaultMemory.used
      freeMemory  <- app.metrics.defaultMemory.free
      totalMemory <- app.metrics.defaultMemory.total

      _ <- app.services.log.info(s"Max Memory: $maxMemory")
      _ <- app.services.log.info(s"Used Memory: $usedMemory")
      _ <- app.services.log.info(s"Free Memory: $freeMemory")
      _ <- app.services.log.info(s"Total Memory: $totalMemory")

      server <- app.http.buildServer(host, port)
    } yield server

  }

  override def stream(args: List[String], requestShutdown: IO[Unit]): Stream[IO, ExitCode] = {
    /*
    val http: Http
    val services: Services
    val metrics: Metrics
     */

    implicitly[FSHandler[ConfigM.Op, Try]]
    implicitly[FSHandler[LoggingM.Op, Try]]
    implicitly[FSHandler[Services.Op, Try]]
    implicitly[FSHandler[Metrics.Op, Try]]
    implicitly[FSHandler[Http.Op, Try]]

    bootstrap[OpscenterApp.Op].interpret[Try].fold(e => Stream.fail(e), _.serve)

  }

}
