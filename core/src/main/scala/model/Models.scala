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
import freestyle.rpc.protocol._
import org.joda.time.DateTime

object models {

  @message
  case class Metric(metric: String, microservice: String, node: String, value: Float, date: Long)

  object Metric {

    object implicits {
      implicit def fromLongToDateTime(l: Long): DateTime = new DateTime(l)
      implicit def fromDateTimeToLong(d: DateTime): Long = d.getMillis
    }

  }

  @message
  case class MetricsList(metrics: List[Metric])

  @message
  case class Microservice(name: String, node: String)

  @message
  case class MicroserviceList(microservices: List[Microservice])
}
