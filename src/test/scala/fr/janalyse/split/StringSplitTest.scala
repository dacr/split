package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class StringSplitTest extends FunSuite with ShouldMatchers {
  
  import StringSplit.tokenizer
  
  test("Direct tokenizer tests") {
    tokenizer("") should equal(Vector())
    tokenizer("   ") should equal(Vector())
    tokenizer("a") should equal(Vector("a"))
    tokenizer("   a   ") should equal(Vector("a"))
    tokenizer("""a b c""") should equal(Vector("a", "b", "c"))
    tokenizer(""" a b  c """) should equal(Vector("a", "b", "c"))
    tokenizer("""a "b c" d""") should equal(Vector("a", "b c", "d"))
    tokenizer("""a "b, c" d""") should equal(Vector("a", "b, c", "d"))
    tokenizer("""a 'b c' d""") should equal(Vector("a", "b c", "d"))
    tokenizer("""a "b c"x"" d""") should equal(Vector("a", """b c"x"""", "d"))
    tokenizer(""" a b, c d """) should equal(Vector("a", "b, c", "d"))
    tokenizer(""" a b; c d """) should equal(Vector("a", "b; c", "d"))
    tokenizer(""" a b,    c d """) should equal(Vector("a", "b,    c", "d"))
    tokenizer(""" a [b   c] d """) should equal(Vector("a", "b   c", "d"))
    tokenizer("""99.99.99.188 - - [14/Oct/2013:14:07:35 +0200] "GET /truc/ping.html HTTP/1.0" 200 7 - 1""") should equal(
        Vector(
            "99.99.99.188",
            "-",
            "-",
            "14/Oct/2013:14:07:35 +0200",
            "GET /truc/ping.html HTTP/1.0",
            "200",
            "7",
            "-",
            "1"
            )
        )
  }
  
  test("Tokenizer on log format specification") {
    val fmt = """%h %l %u %t "%r" %>s %b %D %{X-Forwarded-For}i %{sm_universalid}i "%{User-agent}i" %{msr_sequence_id}i"""
    val tokens = tokenizer(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }

  ignore("Tokenizer on log format specification bis") {
    val fmt = """%h %l %u %t \"%r\" %>s %b %D %{X-Forwarded-For}i %{sm_universalid}i \"%{User-agent}i\" %{msr_sequence_id}i"""
    val tokens = tokenizer(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }
  
  test("perf test") {
    val line="""truc [machin chose] 'gloubs' one, two, three, four 3454Mb long zut oups plouf paf le chien"""
    def now = System.currentTimeMillis()
    val started = now
    var processedTokens = 0L
    var processedLines=0L    
    do {
      val tokens = tokenizer(line)
      processedTokens += tokens.size
      processedLines += 1
    } while (now - started < 10000L)
    val howlong = (now - started)/1000
    info(f"found ${processedTokens/howlong}%,d tokens/second through ${processedLines/howlong}%,d lines/second")
  }
}
