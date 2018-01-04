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
package opscenter

import freestyle.free._
import freestyle.free.config.ConfigM
import freestyle.free.logging._
import org.http4s.implicits._
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder
import cats.effect.IO
import cats.implicits._
import freestyle.metrics.Metrics

@module trait OpscenterApp {
  val http: Http
  val services: Services
  val metrics: Metrics
}

@module trait Services {
  val config: ConfigM
  val log: LoggingM
}

@module trait Http {
  val server: Server
  val endpoints: Endpoints

  def buildServer(host: String, port: Int): FS.Seq[BlazeBuilder[IO]] = {
    for {
      endpoints <- endpoints.build
      server    <- server.getServer(host, port, endpoints)
    } yield server
  }
}

@free trait Server {
  def getServer(host: String, port: Int, endpoints: HttpService[IO]): FS[BlazeBuilder[IO]]
}

@free trait Endpoints {

  def protoMetricModels: FS[HttpService[IO]]
  def protoMicroservicesModels: FS[HttpService[IO]]
  def microservices: FS[HttpService[IO]]
  def healthcheck: FS[HttpService[IO]]
  def streamMetrics: FS[HttpService[IO]]

  def build: FS.Seq[HttpService[IO]] =
    (healthcheck, protoMetricModels, protoMicroservicesModels, streamMetrics, microservices).mapN(
      _ <+> _ <+> _ <+> _ <+> _)

}
