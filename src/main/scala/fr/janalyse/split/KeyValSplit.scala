/*
 * Copyright 2011-2023 David Crosson, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.janalyse.split

import scala.annotation.tailrec
import scala.util.matching.Regex

object KeyValSplit {
  
  private val defaultKeyFinderRE = "(?:^|, )([_a-zA-Z0-9]+)=".r
  /**
   * Smart key val tokenizer, each keyval tuple is separated by a comma,
   * but values may contains space and commas, ...
   * @param input the string from which extract key-value pairs 
   * @param keyFinderRE the regular expression used to extract the keys
   * @return key values map
   */
  def keyValSplit(input: String, keyFinderRE:Regex=defaultKeyFinderRE): Map[String, String] = {
    val keys = keyFinderRE.findAllMatchIn(input).map(_.group(1)).toList
    @tailrec
    def splitItJob(
      remainingKeys: List[String],
      input: String,
      accu: Map[String, String] = Map.empty,
      lastKey: Option[String] = None): Map[String, String] = {
      if (lastKey.isEmpty) {
        val Array(before, after) = input.split(remainingKeys.head + "=", 2)
        splitItJob(remainingKeys.tail, after, accu, Some(remainingKeys.head))
      } else {
        remainingKeys match {
          case Nil => accu + (lastKey.get -> input)
          case key :: tail =>
            val Array(before, after) = input.split(key + "=", 2)
            val value = before.replaceAll(", $", "")
            splitItJob(remainingKeys.tail, after, accu + (lastKey.get -> value), Some(key))
        }
      }
    }
    splitItJob(keys, input)
  }

}
