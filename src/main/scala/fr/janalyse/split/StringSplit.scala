package fr.janalyse.split

import annotation.tailrec

object StringSplit {
  
  /**
   * split a string into a tree of substrings
   * @param input the string to split
   * @param isSeparator is the current char an item separator, the test can only take into account current item being built
   * @param isSubGroup is the current chart the start of a new subgroup, and returns the associated char for end of group
   * @return the tree of substrings
   */
  final def treezer(
      input:String,
      isSeparator:(Char, String)=>Boolean=defaultIsSeparator,
      isSubGroup:(Char)=>Option[Char]=defaultIsSubGroup
      ):Vector[SplitItem] = {
    ???
  }
  
  trait SplitItem {
    def hasChild:Boolean
    def hasContent:Boolean
  }
  
  case class SplitLeaf(content:String) extends SplitItem {
    def hasChild=false
    def hasContent=true
  }
  
  case class SplitNode(children:List[SplitItem]) extends SplitItem {
    def hasChild = !children.isEmpty
    def hasContent=false
  }

  
  def defaultIsSeparator(ch:Char,current:String):Boolean = {
    def isSpace = ch == ' ' || ch == '\t' || ch == '\n' || ch == '\r'
    def doConcat = current.trim.endsWith(",")
    isSpace && ! doConcat
  }
  
  def defaultIsSubGroup(ch:Char):Option[Char] = {
    ch match {
      case '[' => Some(']')
      case '(' => Some(')')
      case '{' => Some('}')
      case _ => None
    }
  }
  
  /**
   * Smart space oriented string split that takes into account comma, quote, double quotes and brackets.
   *
   * @param line the line to split into a vector a substrings
   * @param maxcount max size for the results vector 
   * @return substrings
   */
  @deprecated("Use split method instead of tokenizer", "0.3.2")
  final def tokenizer(line: String,maxcount:Int=0): Vector[String] = tokenize(line.trim, maxcount=maxcount)

  /**
   * Smart space oriented string split that takes into account comma, quote, double quotes and brackets.
   *
   * @param line the line to split into a vector a substrings
   * @param maxcount max size for the results vector 
   * @return vector of substrings
   */
  final def split(line: String, maxcount:Int=0): Vector[String] = tokenize(line.trim, maxcount=maxcount)

  
  @tailrec
  private final def indexWhere(str: String, sz: Int, testme: String => Boolean, pos: Int = 0): Int = {
    if (sz == 0) -1
    else if (testme(str)) pos
    else if (str.head == '\\' && sz > 1) indexWhere(str.tail.tail, sz - 2, testme, pos + 2)
    else indexWhere(str.tail, sz - 1, testme, pos + 1)
  }

  private final def partition(str: String, test: String => Boolean): Tuple2[String, String] = {
    indexWhere(str, str.length(), test) match {
      case -1 => (str, "")
      case i  => (str.substring(0, i), str.substring(i + 1))
    }
  }

  private final def groupedCheck(ch: Char)(testWith: String): Boolean = {
    (ch match {
      case '"'  => '"'
      case '\'' => '\''
      case '['  => ']'
    }) == testWith.head && {
      testWith.size == 1 || spacecheck(testWith.tail.head)
    }
  }
  
  private final def spacecheck(curchar:Char):Boolean = {
    curchar == ' ' || curchar == '\r' || curchar == '\t'
  }

  @tailrec
  private final def partitionSpaceWithoutComma(str: String, currentPos: Int = 0, commafound: Boolean = false, spacefound: Boolean = false): Tuple2[String, String] = {
    if (str.length() == currentPos) (str, "")
    else {
      val curchar = str.charAt(currentPos)
      if (curchar == ',' || curchar == ';') {
        partitionSpaceWithoutComma(str, currentPos + 1, true, spacefound)
      } else if (spacecheck(curchar)) {
        partitionSpaceWithoutComma(str, currentPos + 1, commafound, true)
      } else {
        if (spacefound && !commafound) {
          (str.substring(0, currentPos).trim, str.substring(currentPos))
        } else {
          partitionSpaceWithoutComma(str, currentPos + 1, false, false)
        }
      }
    }
  }

  @tailrec
  private final def tokenize(remain: String, accumulator: Vector[String] = Vector.empty, maxcount:Int): Vector[String] = {
    if (remain.isEmpty()) accumulator
    else if (maxcount>0 && accumulator.size + 1 == maxcount) accumulator :+ remain.trim
    else {
      remain.head match {
        case ' ' | '\r' | '\t' => tokenize(remain.tail, accumulator, maxcount)
        case ch @ ('"' | '\'' | '[') =>
          val (selected, newremain) = partition(remain.tail, groupedCheck(ch))
          tokenize(newremain, accumulator :+ selected, maxcount)
        case _ =>
          val (selected, newremain) = partitionSpaceWithoutComma(remain)
          tokenize(newremain, accumulator :+ selected, maxcount)
      }
    }
  }
}





