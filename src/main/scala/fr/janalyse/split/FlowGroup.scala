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

import util.matching.Regex
import scala.collection.compat._

object FlowGroup {

  /**
   * collection of strings into a shorter one,
   * @param in the flow of strings to process
   * @param startRE a regular expression is used as the separator
   * @param mks string separator to use to groups strings together
   * @return the new flow of stream
   */
  def restrings(in: Iterable[String], startRE: Regex, mks: String): Iterable[String] =
    restrings(in, startRE.findFirstIn(_).isDefined, mks)

  def restringsit(in: Iterator[String], startRE: Regex, mks: String): Iterator[String] =
    restringsit(in, startRE.findFirstIn(_).isDefined, mks)

  /**
   * collection of strings into a shorter one
   * @param in the flow of strings to process
   * @param startTest test function to know if the given string marks the beginning of a new string block
   * @param mks string separator to use to groups strings together
   * @return the new flow of stream
   */
  def restrings(in: Iterable[String], startTest: String => Boolean, mks: String): Iterable[String] = {
    def build(first: String, nexts: List[String]) = (first :: nexts).mkString(mks)
    def theTest(part: String) = if (startTest(part)) Some(part) else None
    reassemble(in, theTest, build)
  }

  def restringsit(in: Iterator[String], startTest: String => Boolean, mks: String): Iterator[String] = {
    def build(first: String, nexts: List[String]) = (first :: nexts).mkString(mks)
    def theTest(part: String) = if (startTest(part)) Some(part) else None
    reassembleit(in, theTest, build)
  }

  /**
   * The generic function that groups strings into larger of something else
   * @param input the flow of strings to process
   * @param startTest function that returns something if and only if the given string marks the beginning of a string block
   * @param build function that build a new output item made of the start entry given by "startTest" and the following strings
   * @return the new flow of something else
   */
  def reassemble[F, R](
    input: Iterable[String],
    startTest: String => Option[F],
    build: (F, List[String]) => R): Iterable[R] = {
    def inputWithTest: Iterable[(Option[F], String)] = input.map(i => startTest(i) -> i)
    def worker(curinput: => Iterable[(Option[F], String)]): Iterable[R] = {
      curinput.headOption match {
        case None            => Iterable.empty
        case Some((None, _)) => worker(curinput.tail)
        case Some((Some(f), _)) =>
          val (selected, remain) = curinput.tail.span { case (x, _) => x.isEmpty }
          val nexts = selected.map { case (_, l) => l }.toList
          Iterable(build(f, nexts)) ++  worker(remain)
      }
    }
    worker(inputWithTest)
  }

  def reassembleit[F, R](
    input: Iterator[String],
    startTest: String => Option[F],
    build: (F, List[String]) => R): Iterator[R] = {
    new Iterator[R] {
      private var readAhead:Option[F]=None
      // init, drop any invalid first lines, not containing the start marker
      while(readAhead.isEmpty && input.hasNext) readAhead = startTest(input.next)
      
      def hasNext: Boolean = readAhead.isDefined
      def next(): R = {
        val nextone = readAhead
        var nextextras = List.empty[String]
        readAhead=None
        while(readAhead.isEmpty && input.hasNext) {
          val currentline = input.next
          readAhead = startTest(currentline)
          if (readAhead.isEmpty) nextextras :+= currentline
        }
        build(nextone.get, nextextras)
      }
    }
  }
  
}

