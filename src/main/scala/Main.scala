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

import freestyle.opscenter.runtime.implicits._
import freestyle._
import freestyle.opscenter._
import freestyle.implicits._
import freestyle.config.implicits._
import cats.implicits._
import cats.syntax._
import cats.~>
import fs2.{Stream, Task}
import org.http4s._
import org.http4s.dsl._
import org.http4s.HttpService
import org.http4s.dsl.Root
import org.http4s.server.blaze._
import org.http4s.server.syntax._
import org.http4s.util.StreamApp
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Await
import scala.concurrent.duration.Duration

object Main extends StreamApp {

  def bootstrap[T[_]](
      implicit app: OpscenterApp[T],
      handler: T ~> Future): FreeS[T, BlazeBuilder] = {

    for {
      config <- app.services.config.load
      host = config.string("http.host").getOrElse("localhost")
      port = config.int("http.port").getOrElse(8080)
      metrics   <- app.metrics.readMetrics
      endpoints <- app.server.getEndpoints(metrics)
      server    <- app.server.getServer(host, port, endpoints)
    } yield server

  }

  override def stream(args: List[String]): Stream[Task, Nothing] =
    Await.result(bootstrap[OpscenterApp.Op].interpret[Future], Duration.Inf).serve

}
