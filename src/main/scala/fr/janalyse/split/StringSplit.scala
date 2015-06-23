package fr.janalyse.split

import annotation.tailrec

object StringSplit {
  
  /**
   * split a string into a tree of substrings
   *    
   * @param input the string to split
   * @return the tree of substrings
   */
  final def treezer(
      input:String,
      isSeparator:(Char)=>Boolean=defaultIsSeparator,
      isQuoted:(Char)=>Option[Char]=defaultIsQuoted,
      isSubGroup:(Char)=>Option[Char]=defaultIsSubGroup,
      concatCheck:(String)=>Boolean=defaultConcatCheck
      ):SplitItem = {
    val it = input.trim.iterator
    
    @tailrec
    def worker(buf:String, accumulator:List[SplitItem]):SplitItem = {
      if (it.hasNext) {
        val ch = it.next()
        if (isSeparator(ch) && concatCheck(buf)) {
          worker(buf+ch, accumulator)
        } else if (isSeparator(ch)) {
          worker("", accumulator:+SplitWord(buf))
        } else {
          worker(buf+ch, accumulator)
        }
      } else {
        if (buf.isEmpty()) SplitNode(accumulator)
        else SplitNode(accumulator:+SplitWord(buf))
      }
    }
    
    worker("",Nil)
  }
  
  sealed trait SplitItem {
    def hasChild:Boolean
    def hasContent:Boolean
  }
  
  case class SplitWord(content:String) extends SplitItem {
    def hasChild=false
    def hasContent=true
  }
  
  case class SplitNode(children:List[SplitItem]) extends SplitItem {
    def hasChild = !children.isEmpty
    def hasContent=false
  }
  object SplitNode {
    def apply(child:SplitItem):SplitNode = SplitNode(child::Nil)
    def apply():SplitNode = SplitNode(Nil)
  }

  def defaultIsQuoted(ch:Char):Option[Char]=
    ch match {
      case '"' | '\'' => Some(ch)
      case _ => None
    }
  
  def defaultIsSeparator(ch:Char):Boolean =
    ch == ' ' || ch == '\r' || ch == '\n' || ch == '\t'
  
  
  def defaultConcatCheck(current:String):Boolean = 
    current.trim.endsWith(",")
  
  
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
  private final def indexWhere(str: String, look4char:Char, pos: Int = 0): Int = {
    val sz = str.size - pos
    if (sz == 0) -1
    else if (str(pos)==look4char && (sz==1 || spacecheck(str(pos+1)))) pos
    else if (str.head == '\\' && sz > 1) indexWhere(str, look4char, pos + 2)
    else indexWhere(str, look4char, pos + 1)
  }

  private final def partition(str: String, pos:Int, look4char:Char): Tuple2[String, Int] = {
    indexWhere(str, look4char, pos=pos) match {
      case -1 => (str.substring(pos), str.size)
      case i  => (str.substring(pos, i), i + 1)
    }
  }

  private final def charCounterPart(ch:Char):Char = {
    (ch match {
      case '"'  => '"'
      case '\'' => '\''
      case '['  => ']'
      case '('  => ')'
      case '{'  => '}'  
    })
  }
  
//  private final def groupedCheck(ch: Char)(testWith: String, pos:Int): Boolean = {
//    (ch match {
//      case '"'  => '"'
//      case '\'' => '\''
//      case '['  => ']'
//      case '('  => ')'
//      case '{'  => '}'  
//    }) == testWith(pos) && {
//      testWith.size - pos == 1 || spacecheck(testWith(pos+1))
//    }
//  }
  
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
          val (selected, newpos) = partition(me, pos+1, charCounterPart(ch))
          tokenize(me, maxcount, newpos, accumulator :+ selected)
      } else {
          val (selected, newpos) = partitionSpaceWithoutComma(me, pos)
          tokenize(me, maxcount, newpos, accumulator :+ selected)
      }

    }
  }  
}





