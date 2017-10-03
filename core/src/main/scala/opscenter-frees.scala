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
import freestyle.opscenter.model.Metric
import org.http4s.HttpService
import org.http4s.server.blaze.BlazeBuilder

// Modules
@module trait OpscenterApp {
  val server: ServerM
  val metrics: MetricsM
  val services: ServicesM
}

@module
trait ServicesM {
  val config: ConfigM
}

// Algebras
@free trait ServerM {
  def getServer(host: String, port: Int, endpoints: HttpService): FS[BlazeBuilder]
  def getEndpoints(metrics: List[Metric[Float]]): FS[HttpService]
}

@free trait MetricsM {
  def readMetrics: FS[List[Metric[Float]]]
}
