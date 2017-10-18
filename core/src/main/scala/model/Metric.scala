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
package model

import freestyle.opscenter.protobuf.metric.{MetricProto, MetricProtoList}

case class Metric(metric: String, microservice: String, node: String, value: Float, date: Long) {
  def toProto: MetricProto = MetricProto(
    metric = metric,
    microservice = microservice,
    node = node,
    value = value,
    date = date
  )
}

object Metric {

  def apply(proto: MetricProto): Metric = Metric(
    metric = proto.metric,
    microservice = proto.microservice,
    node = proto.node,
    value = proto.value,
    date = proto.date
  )

  def apply(bytes: Array[Byte]): Metric =
    Metric(MetricProto.parseFrom(bytes))

}

case class MetricsList(metricsList: List[MetricProto]) {
  def toProto: MetricProtoList = MetricProtoList(
    metricsList = metricsList
  )
}

object MetricsList {

  def apply(proto: MetricProtoList): MetricsList = MetricsList(
    metricsList = proto.metricsList
  )

  def apply(bytes: Array[Byte]): MetricsList =
    MetricsList(MetricProtoList.parseFrom(bytes))

}
