package fr.janalyse.split

import util.matching.Regex

object FlowGroup {

  /**
   * collection of strings into a shorter one,
   * @in the flow of strings to process
   * @param startRE a regular expression is used as the separator
   * @param mks string separator to use to groups strings together
   * @return the new flow of stream
   */
  def restrings(in: Stream[String], startRE: Regex,mks:String): Stream[String] =
    restrings(in, startRE.findFirstIn(_).isDefined,mks)

  /**
   * collection of strings into a shorter one
   * @in the flow of strings to process
   * @param startTest test function to know if the given string marks the beginning of a new string block
   * @param mks string separator to use to groups strings together
   * @return the new flow of stream
   */
  def restrings(in: Stream[String], startTest: String => Boolean, mks:String): Stream[String] = {
    def build(first: String, nexts: List[String]) = (first :: nexts).mkString(mks)
    def theTest(part: String) = if (startTest(part)) Some(part) else None
    reassemble(in, theTest, build)
  }

  /**
   * The generic function that groups strings into larger a something else
   * @input the flow of strings to process
   * @param startTest function that returns something if and only if the given string marks the beginning of a string block
   * @param build function that build a new output item made of the start entry given by "startTest" and the following strings
   * @return the new flow of something else
   */
  def reassemble[F, R](
    input: Stream[String],
    startTest: String => Option[F],
    build: (F, List[String]) => R): Stream[R] = {

    def inputWithTest: Stream[(Option[F], String)] = input.map(i => startTest(i) -> i)
    def worker(curinput: Stream[(Option[F], String)]): Stream[R] = {
      curinput.headOption match {
        case None            => Stream.empty
        case Some((None, _)) => worker(curinput.tail)
        case Some((Some(f), _)) =>
          val (selected, remain) = curinput.tail.span { case (x, _) => x.isEmpty }
          val nexts = selected.map { case (_, l) => l }.toList
          build(f, nexts) #:: worker(remain)
      }
    }
    worker(inputWithTest)
  }

}

