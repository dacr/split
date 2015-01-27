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
