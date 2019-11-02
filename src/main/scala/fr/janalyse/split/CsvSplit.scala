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
    @tailrec def worker(me: String, accumulator: Vector[String] = Vector.empty): Vector[String] = {
      if (me.isEmpty) accumulator
      else {
        val ch = me.head
        if (ch.isSpaceChar || ch == sep) worker(me.tail, accumulator)
        else if (maxcount > 0 && accumulator.size + 1 == maxcount) accumulator :+ me.trim
        else if (ch == '"' || ch == '\'') {
          val (left, right) = me.tail.span(_ != ch)
          worker(right.tail, accumulator :+ left)
        } else {
          val (left, right) = me.span(_ != sep)
          worker(right, accumulator :+ left.trim)
        }
      }
    }
    worker(line)
  }
}
