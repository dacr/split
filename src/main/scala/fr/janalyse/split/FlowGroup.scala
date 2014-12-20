package fr.janalyse.split

import util.matching.Regex

object FlowGroup {

  /**
   * collection of strings into a shorter one,
   * a regular expression is used as the separator
   */
  def restrings(in: Stream[String], startRE: Regex,mks:String): Stream[String] =
    restrings(in, startRE.findFirstIn(_).isDefined,mks)

  def restrings(in: Stream[String], startTest: String => Boolean, mks:String): Stream[String] = {
    def build(first: String, nexts: List[String]) = (first :: nexts).mkString(mks)
    def theTest(part: String) = if (startTest(part)) Some(part) else None
    reassemble(in, theTest, build)
  }

  /**
   * The generic function that groups strings into larger something else
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

