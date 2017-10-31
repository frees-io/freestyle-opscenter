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
package metrics
package runtime
package default

import freestyle._
import freestyle.implicits._
import freestyle.metrics.models.{Metric, MetricsList}
import freestyle.metrics.models.Metric.implicits._
import cats.effect.IO
import scala.concurrent.duration._
import org.joda.time.DateTime
import scala.util.Random

object implicits {

  implicit def defaultHandler[M[_]](implicit C: Capture[M]): Default.Handler[M] =
    new Default.Handler[M] {

      private val mb      = 1024 * 1024
      private val runtime = Runtime.getRuntime

      def usedMemory: M[Float] = C.capture((runtime.totalMemory - runtime.freeMemory) / mb)

      def freeMemory: M[Float] = C.capture(runtime.freeMemory / mb)

      def totalMemory: M[Float] = C.capture(runtime.totalMemory / mb)

      def maxMemory: M[Float] = C.capture(runtime.maxMemory / mb)

    }

}
