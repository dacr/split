package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class StringSplitTest extends FunSuite with ShouldMatchers {

  import StringSplit._

  test("Treezer tests") {
    treezer("") should equal(SplitNode())
    treezer(" \t\r\n") should equal(SplitNode())
    treezer("a") should equal(SplitNode(SplitWord("a")))
    treezer(" a b ") should equal(SplitNode(SplitWord("a"), SplitWord("b")))
    treezer("a, b") should equal(SplitNode(SplitWord("a, b")))
    treezer("A [B]")   should equal(SplitNode(SplitWord("A"), SplitNode(SplitWord("B"))))
    treezer("A [B] C") should equal(SplitNode(SplitWord("A"), SplitNode(SplitWord("B")), SplitWord("C")))
    treezer("A [B(B1 B2)] C") should equal(
        SplitNode(
            SplitWord("A"),
            SplitNode(
                SplitWord("B"),
                SplitNode(
                    SplitWord("B1"),
                    SplitWord("B2")
                    )
                ),
            SplitWord("C")))

  }
  
  test("Direct split tests") {
    split("") should equal(Vector())
    split("   ") should equal(Vector())
    split("a") should equal(Vector("a"))
    split("   a   ") should equal(Vector("a"))
    split("""a b c""") should equal(Vector("a", "b", "c"))
    split(""" a b  c """) should equal(Vector("a", "b", "c"))
    split("""a "b c" d""") should equal(Vector("a", "b c", "d"))
    split("""a "b, c" d""") should equal(Vector("a", "b, c", "d"))
    split("""a 'b c' d""") should equal(Vector("a", "b c", "d"))
    split("""a "b c"x"" d""") should equal(Vector("a", """b c"x"""", "d"))
    split(""" a b, c d """) should equal(Vector("a", "b, c", "d"))
    split(""" a b; c d """) should equal(Vector("a", "b; c", "d"))
    split(""" a b,    c d """) should equal(Vector("a", "b,    c", "d"))
    split(""" a [b   c] d """) should equal(Vector("a", "b   c", "d"))
    split("""99.99.99.188 - - [14/Oct/2013:14:07:35 +0200] "GET /truc/ping.html HTTP/1.0" 200 7 - 1""") should equal(
      Vector(
        "99.99.99.188",
        "-",
        "-",
        "14/Oct/2013:14:07:35 +0200",
        "GET /truc/ping.html HTTP/1.0",
        "200",
        "7",
        "-",
        "1"))
  }

  test("extends split tests") {
    split("1 2 3", 2) should equal(Vector("1", "2 3"))
    split("1 2 3", 1) should have size (1)
    split("1 [A B C] 3 4", 3) should equal(Vector("1", "A B C", "3 4"))
  }

  test("split on log format specification") {
    val fmt = """%h %l %u %t "%r" %>s %b %D %{X-Forwarded-For}i %{sm_universalid}i "%{User-agent}i" %{msr_sequence_id}i"""
    val tokens = split(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }

  ignore("split on log format specification bis") {
    val fmt = """%h %l %u %t \"%r\" %>s %b %D %{X-Forwarded-For}i %{sm_universalid}i \"%{User-agent}i\" %{msr_sequence_id}i"""
    val tokens = split(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }

  test("split test") {
    for { a <- 1 to 5 } {
      val line = """truc [machin chose] 'gloubs' one, two, three, four 3454Mb long zut oups plouf paf le chien"""
      def now = System.currentTimeMillis()
      val started = now
      var processedTokens = 0L
      var processedLines = 0L
      do {
        val tokens = split(line)
        processedTokens += tokens.size
        processedLines += 1
      } while (now - started < 5000L)
      val howlong = (now - started) / 1000
      info(f"found#$a : ${processedTokens / howlong}%,d tokens/second through ${processedLines / howlong}%,d lines/second")
      Thread.sleep(1000)
    }
  }
}
