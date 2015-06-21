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
  private final def indexWhere(str: String, testme: (String, Int) => Boolean, pos: Int = 0): Int = {
    val sz = str.size - pos
    if (sz == 0) -1
    else if (testme(str, pos)) pos
    else if (str.head == '\\' && sz > 1) indexWhere(str, testme, pos + 2)
    else indexWhere(str, testme, pos + 1)
  }

  private final def partition(str: String, pos:Int, test: (String, Int) => Boolean): Tuple2[String, Int] = {
    indexWhere(str, test, pos=pos) match {
      case -1 => (str.substring(pos), str.size)
      case i  => (str.substring(pos, i), i + 1)
    }
  }

  private final def groupedCheck(ch: Char)(testWith: String, pos:Int): Boolean = {
    (ch match {
      case '"'  => '"'
      case '\'' => '\''
      case '['  => ']'
      case '('  => ')'
      case '{'  => '}'  
    }) == testWith(pos) && {
      testWith.size - pos == 1 || spacecheck(testWith(pos+1))
    }
  }
  
  private final def spacecheck(curchar:Char):Boolean = {
    curchar == ' ' || curchar == '\r' || curchar == '\t'
  }

  
  @tailrec
  private final def partitionSpaceWithoutComma(str: String, pos:Int = 0, relative: Int = 0, commafound: Boolean = false, spacefound: Boolean = false): Tuple2[String, Int] = {
    if (str.size == pos+relative) (str.substring(pos), pos+relative)
    else {
      val curchar = str.charAt(pos+relative)
      if (curchar == ',' || curchar == ';') {
        partitionSpaceWithoutComma(str, pos, relative + 1, true, spacefound)
      } else if (spacecheck(curchar)) {
        partitionSpaceWithoutComma(str, pos, relative + 1, commafound, true)
      } else {
        if (spacefound && !commafound) {
          (str.substring(pos, pos+relative).trim, pos+relative)
        } else {
          partitionSpaceWithoutComma(str, pos, relative + 1, false, false)
        }
      }
    }
  }
  
  @tailrec
  private final def tokenize(me: String, maxcount:Int, pos:Int=0, accumulator: Vector[String] = Vector.empty): Vector[String] = {
    if (pos >= me.size) accumulator
    else if (maxcount>0 && accumulator.size + 1 == maxcount) accumulator :+ me.substring(pos).trim()
    else {      
      val ch = me(pos)
      if (ch == ' ' || ch == '\r' | ch == '\t') tokenize(me, maxcount, pos+1, accumulator)
      else if ( ch == '"' || ch== '\'' || ch == '[' || ch== '(' || ch == '{') {
          val (selected, newpos) = partition(me, pos+1, groupedCheck(ch))
          tokenize(me, maxcount, newpos, accumulator :+ selected)
      } else {
          val (selected, newpos) = partitionSpaceWithoutComma(me, pos)
          tokenize(me, maxcount, newpos, accumulator :+ selected)
      }

    }
  }  
}





