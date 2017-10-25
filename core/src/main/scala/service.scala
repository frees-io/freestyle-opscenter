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
import monix.reactive.Observable

@option(name = "java_package", value = "routeguide", quote = true)
@option(name = "java_multiple_files", value = "true", quote = false)
@option(name = "java_outer_classname", value = "RouteGuide", quote = true)
object protocols {

  @message
  case class Feature(name: String, location: String)

  @free
  @service
  @debug
  trait OpscenterService {
    @rpc def getFeature(point: String): FS[Feature]
  }

}