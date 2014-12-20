package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.matchers.ShouldMatchers

class StringSplitTest extends FunSuite with ShouldMatchers {
  
  import StringSplit.{tokenizer  => tok}
  
  test("Direct tokenizer tests") {
    tok("") should equal(Vector())
    tok("   ") should equal(Vector())
    tok("a") should equal(Vector("a"))
    tok("   a   ") should equal(Vector("a"))
    tok("""a b c""") should equal(Vector("a", "b", "c"))
    tok(""" a b  c """) should equal(Vector("a", "b", "c"))
    tok("""a "b c" d""") should equal(Vector("a", "b c", "d"))
    tok("""a "b, c" d""") should equal(Vector("a", "b, c", "d"))
    tok("""a 'b c' d""") should equal(Vector("a", "b c", "d"))
    tok("""a "b c"x"" d""") should equal(Vector("a", """b c"x"""", "d"))
    tok(""" a b, c d """) should equal(Vector("a", "b, c", "d"))
    tok(""" a b; c d """) should equal(Vector("a", "b; c", "d"))
    tok(""" a b,    c d """) should equal(Vector("a", "b,    c", "d"))
    tok(""" a [b   c] d """) should equal(Vector("a", "b   c", "d"))
    tok("""99.99.99.188 - - [14/Oct/2013:14:07:35 +0200] "GET /truc/ping.html HTTP/1.0" 200 7 - 1""") should equal(
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
    val tokens = tok(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }

  ignore("Tokenizer on log format specification bis") {
    val fmt = """%h %l %u %t \"%r\" %>s %b %D %{X-Forwarded-For}i %{sm_universalid}i \"%{User-agent}i\" %{msr_sequence_id}i"""
    val tokens = tok(fmt)
    assert(tokens.size === 12, "")
    assert(tokens(4) === "%r")
  }
}
