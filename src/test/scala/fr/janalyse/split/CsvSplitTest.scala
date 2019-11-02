package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.Matchers._

class CsvSplitTest extends FunSuite {
  import CsvSplit._

  test("Direct csv split tests") {
    split("") should equal(Vector())
    split("   ") should equal(Vector())
    split("a") should equal(Vector("a"))
    split("   a   ") should equal(Vector("a"))
    split("""a, b, c""") should equal(Vector("a", "b", "c"))
    split(""" a, b,  c """) should equal(Vector("a", "b", "c"))
    split("""a, 'b c', d""") should equal(Vector("a", "b c", "d"))
    split("""a, "b c", d""") should equal(Vector("a", "b c", "d"))
    split("""a ,"b, c" ,d""") should equal(Vector("a", "b, c", "d"))
  }

  test("extends csv split tests") {
    split("1, 2, 3") should equal(Vector("1", "2", "3"))
    split("1, 2, 3", maxcount = 1) should equal(Vector("1, 2, 3"))
    split("1, 2, 3", maxcount = 2) should equal(Vector("1", "2, 3"))
    split("1, 2, 3", maxcount = 3) should equal(Vector("1", "2", "3"))
  }
}
