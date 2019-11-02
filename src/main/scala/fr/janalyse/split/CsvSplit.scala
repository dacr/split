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
          worker(right.tail.trim.drop(1), accumulator :+ left)
        } else {
          val (left, right) = me.span(_ != sep)
          worker(right.trim.drop(1), accumulator :+ left.trim, !right.isEmpty)
        }
      }
    }
    worker(line)
  }
}
