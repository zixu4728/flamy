/*
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
 *
 */

package com.flaminem.flamy.exec.actions

import com.flaminem.flamy.exec.utils.{Action, SkipAction}
import com.flaminem.flamy.model.names.TableName

/**
  * A RunAction is an action run by the GraphRunner.
  * It may be a PopulateRunAction, or a SkipRunAction when there is no Populate to run.
  */
trait RunAction extends Action {

  def tableName: TableName

  override def toString: String = name

}

object RunAction {

  /**
    * A default ordering for RunAction.
    * SkipActions come first
    * @tparam A
    * @return
    */
  implicit def ordering[A <: RunAction]: Ordering[A] = {
    new Ordering[A] {
      override def compare(x: A, y: A): Int = {
        (x, y) match {
          case (l:SkipAction, r:PopulateAction) => -1
          case (l:PopulateAction, r:SkipAction) => 1
          case (l:PopulateAction, r:PopulateAction) => PopulateAction.ordering.compare(l, r)
          case (l, r) => Action.ordering.compare(l, r)
        }
      }
    }
  }

}
