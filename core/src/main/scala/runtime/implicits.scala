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

import freestyle.opscenter.model.Metric
import org.http4s.{HttpService, _}
import org.http4s.dsl._
import org.http4s.dsl.Root
import org.http4s.server.blaze._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.{Source => SourceIO}

object implicits {

  implicit val metricsHandler: MetricsM.Handler[Future] = new MetricsM.Handler[Future] {
    def readMetrics: Future[List[Metric[Float]]] = {
      val fileStream = getClass.getResourceAsStream("/metrics.txt")
      Future(SourceIO.fromInputStream(fileStream).getLines.toList.map(lineToTextMessage))
    }

    private def lineToTextMessage(line: String): Metric[Float] = {
      val columns = line.split(" ")
      new Metric[Float](columns(0), columns(2), columns(3).toFloat, columns(1).toLong)
    }
  }

  implicit val serverMHandler: ServerM.Handler[Future] = new ServerM.Handler[Future] {

    def getServer(host: String, port: Int, endpoints: HttpService): Future[BlazeBuilder] =
      Future(BlazeBuilder.bindHttp(port, host).mountService(endpoints, "/metrics"))

    def getEndpoints(metrics: List[Metric[Float]]): Future[HttpService] =
      Future(endpointsServices(metrics))

    private def endpointsServices(metrics: List[Metric[Float]]): HttpService = HttpService {
      case GET -> Root / "export"      => Ok(metrics.map(_.toString()) mkString "\n")
      case GET -> Root / "healthcheck" => Ok(s"Works fine.")
    }
  }

}
