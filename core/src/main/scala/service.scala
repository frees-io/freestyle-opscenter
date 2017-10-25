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
import freestyle.rpc.protocol.{rpc, stream, _}
import monix.reactive.Observable

@option(name = "java_package", value = "routeguide", quote = true)
@option(name = "java_multiple_files", value = "true", quote = false)
@option(name = "java_outer_classname", value = "RouteGuide", quote = true)
object protocols {

  @message
  case class Book(isbn: Int, title: String, author: String)

  @message
  case class GetBookRequest(isbn: Int)
  @message
  case class QueryBooksRequest(author_prefix: String)

  @free
  @service
  @debug
  trait OpscenterService {

    @rpc
    @stream[ResponseStreaming.type]
    def getBook(book: GetBookRequest): FS[Observable[Book]]

    @rpc
    @stream[ResponseStreaming.type]
    def queryBooks(query: QueryBooksRequest): FS[Observable[Book]]

  }

}