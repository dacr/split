package fr.janalyse.split

import annotation.tailrec

object StringSplit {

  def tokenizer(line: String): Vector[String] = tokenize(line.trim)

    @tailrec
    private def indexWhere(str: String, sz: Int, testme: String => Boolean, pos: Int = 0): Int = {
      if (sz == 0) -1
      else if (testme(str)) pos
      else if (str.head == '\\' && sz > 1) indexWhere(str.tail.tail, sz - 2, testme, pos + 2)
      else indexWhere(str.tail, sz - 1, testme, pos + 1)
    }

    private def partition(str: String, test: String => Boolean): Tuple2[String, String] = {
      indexWhere(str, str.length(), test) match {
        case -1 => (str, "")
        case i  => (str.substring(0, i), str.substring(i + 1))
      }
    }


  private def groupedCheck(ch: Char)(testWith: String): Boolean = {
    (ch match {
      case '"'  => '"'
      case '\'' => '\''
      case '['  => ']'
    }) == testWith.head && {
      testWith.size==1 || " \r\t".contains(testWith.tail.head)
    }
  }


   @tailrec
   private def partitionSpaceWithoutComma(str: String, currentPos: Int = 0, commafound: Boolean = false, spacefound: Boolean = false): Tuple2[String, String] = {
      if (str.length() == currentPos) (str, "")
      else {
        val curchar = str.charAt(currentPos)
        if (",;".contains(curchar)) partitionSpaceWithoutComma(str, currentPos + 1, true, spacefound)
        else if (" \r\t".contains(curchar)) partitionSpaceWithoutComma(str, currentPos + 1, commafound, true)
        else {
          if (spacefound && !commafound) {
            (str.substring(0, currentPos).trim, str.substring(currentPos))
          } else {
            partitionSpaceWithoutComma(str, currentPos + 1, false, false)
          }
        }
      }
    }

    @tailrec
    private def tokenize(remain: String, accumulator: Vector[String] = Vector.empty): Vector[String] = {
      if (remain.isEmpty()) accumulator
      else {
        remain.head match {
          case ' ' | '\r' | '\t' => tokenize(remain.tail, accumulator)
          case ch@('"' | '\'' | '[') =>
            //val (selected, newremain) = partition(remain.tail, _ == grouping(ch))
            val (selected, newremain) = partition(remain.tail, groupedCheck(ch))
            tokenize(newremain, accumulator :+ selected)
          case _ =>
            val (selected, newremain) = partitionSpaceWithoutComma(remain)
            tokenize(newremain, accumulator :+ selected)
        }
      }
    }
  }





