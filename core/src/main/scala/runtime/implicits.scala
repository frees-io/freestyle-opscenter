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
import fs2.util.{Attempt, Catchable, Suspendable}

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.io.{Source => SourceIO}
import scala.meta.Term.{Return, Throw}

object implicits {

  implicit val metricsHandler: MetricsM.Handler[Future] = new MetricsM.Handler[Future] {
    def readMetrics: Future[List[String]] = {
      val fileStream = getClass.getResourceAsStream("/metrics.txt")
      Future(SourceIO.fromInputStream(fileStream).getLines.toList.map(lineToTextMessage))
    }

    private def lineToTextMessage(line: String): String = {
      val columns = line.split(" ")
      new Metric[Float](columns(0), columns(2), columns(3).toFloat, columns(1).toLong).toString()
    }
  }

  implicit val serverMHandler: ServerM.Handler[Future] = new ServerM.Handler[Future] {
    def start: Future[String] = Future("Starting server ...")
  }

}
