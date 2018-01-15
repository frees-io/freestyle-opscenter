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
package services

import freestyle.opscenter.models.{Microservice, MicroserviceList}

object microservices {

  def getMicroservices: MicroserviceList = {

    // TODO: Pending to implement with freestyle-cassandra
    val node1 = Microservice("microservice-1", "node-1")
    val node2 = Microservice("microservice-1", "node-2")
    val node3 = Microservice("microservice-2", "node-1")
    val node4 = Microservice("microservice-2", "node-2")
    val node5 = Microservice("microservice-2", "node-3")
    val node6 = Microservice("microservice-2", "node-4")
    val node7 = Microservice("microservice-3", "node-1")
    val node8 = Microservice("microservice-3", "node-2")
    val node9 = Microservice("microservice-4", "node-1")

    MicroserviceList(
        node1 ::
        node2 ::
        node3 ::
        node4 ::
        node5 ::
        node6 ::
        node7 ::
        node8 ::
        node9 ::
        Nil)

  }
}
