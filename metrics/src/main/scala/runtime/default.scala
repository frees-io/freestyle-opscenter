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
package metrics
package runtime
package default

import freestyle.free._
import freestyle.free.implicits._
import freestyle.metrics.models.{Metric, MetricsList}
import freestyle.metrics.models.Metric.implicits._
import cats.effect.IO
import scala.concurrent.duration._
import org.joda.time.DateTime
import scala.util.Random

object implicits {

  implicit def defaultHandler[M[_]](implicit C: Capture[M]): DefaultMemoryMetrics.Handler[M] =
    new DefaultMemoryMetrics.Handler[M] {

      private val runtime = Runtime.getRuntime

      def used: M[Float] = C.capture((runtime.totalMemory - runtime.freeMemory))

      def free: M[Float] = C.capture(runtime.freeMemory)

      def total: M[Float] = C.capture(runtime.totalMemory)

      def max: M[Float] = C.capture(runtime.maxMemory)

    }

}
