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
package runtime
package server

import cats.Applicative
import org.http4s.HttpService
import org.http4s.server.blaze._
import cats.effect.IO

object implicits {

  import cats.syntax.applicative._

  implicit def serverMHandler[M[_]: Applicative]: Server.Handler[M] = new Server.Handler[M] {

    def getServer(host: String, port: Int, endpoints: HttpService[IO]): M[BlazeBuilder[IO]] =
      BlazeBuilder[IO]
        .bindHttp(port, host)
        .withWebSockets(true)
        .mountService(endpoints, "/")
        .pure[M]

  }

}
