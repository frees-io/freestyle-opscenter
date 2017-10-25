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

package routeguide
package handlers

import cats.~>
import freestyle.Capture
import freestyle.opscenter.protocols._
import journal.Logger
import monix.eval.Task
import monix.reactive.Observable

class OpscenterServiceHandler[F[_]](implicit C: Capture[F], T2F: Task ~> F)
    extends OpscenterService.Handler[F] {

  val logger: Logger = Logger[this.type]

  override protected[this] def getBook(book: GetBookRequest): F[Observable[Book]] =
    C.capture(Observable(Book(123, "b", "aa")))

  override protected[this] def queryBooks(query: QueryBooksRequest): F[Observable[Book]] =
    C.capture(Observable(Book(123, "b", "aa")))

}
