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

object CsvSplit {

  /**
   * Smart CSV oriented line split that takes into account quote, double quotes.
   *
   * @param line     the line to split into a vector a substrings
   * @param sep      the cell separator
   * @param maxcount max size for the results vector
   * @return vector of substrings
   */
  final def split(line: String, sep: Char = ',', maxcount: Int = 0): Vector[String] = {
    def isSpace(ch: Char): Boolean = ch.isSpaceChar || ch == '\r' || ch == '\t'
    @tailrec
    def worker(me: String, accumulator: Vector[String] = Vector.empty, hasNext:Boolean=false): Vector[String] = {
      if (me.isEmpty) {
        if (hasNext) accumulator:+"" else accumulator
      } else {
        val ch = me.head
        if (isSpace(ch)) worker(me.tail, accumulator, hasNext)
        else if (maxcount > 0 && accumulator.size + 1 == maxcount) accumulator :+ me.trim
        else if (ch == '"' || ch == '\'') {
          val (left, right) = me.tail.span(_ != ch)
          worker(right.drop(1).trim.drop(1), accumulator :+ left)
        } else {
          val (left, right) = me.span(_ != sep)
          worker(right.trim.drop(1), accumulator :+ left.trim, !right.isEmpty)
        }
      }
    }
    worker(line)
  }
}
