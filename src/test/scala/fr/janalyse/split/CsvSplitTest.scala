package fr.janalyse.split

import org.scalatest.FlatSpec
import org.scalatest.Matchers._

class CsvSplitTest extends FlatSpec {
  import CsvSplit._

  "csv cell split" should "support empty cell" in {
    split("") should equal(Vector())
    split("   ") should equal(Vector())
    split(" \t\r ") should equal(Vector())
  }

  it should "support single cell" in {
    split("a") should equal(Vector("a"))
    split("   a   ") should equal(Vector("a"))
    split("abcdef") should equal(Vector("abcdef"))
  }

  it should "be able to split multiple simple content cells" in {
    split("""a, b, c""") should equal(Vector("a", "b", "c"))
    split(""" a, b,  c """) should equal(Vector("a", "b", "c"))
    split("""a, b c, d""") should equal(Vector("a", "b c", "d"))
    split("""a,  b   c , d""") should equal(Vector("a", "b   c", "d"))
  }

  it should "allow empty cells" in {
    split(",a") should equal(Vector("", "a"))
    split("a,") should equal(Vector("a", ""))
    split(",a,") should equal(Vector("", "a", ""))
    split("  ,  a") should equal(Vector("", "a"))
    split("  ,  a,,") should equal(Vector("", "a", "", ""))
  }

  it should "allow any kind of content in cells even cells separator when double or single quotes are used" in {
    split("""a,'b c', d""") should equal(Vector("a", "b c", "d"))
    split("""a,  'b c'  , d""") should equal(Vector("a", "b c", "d"))
    split("""a, "b c", d""") should equal(Vector("a", "b c", "d"))
    split("""a ,"b, c" ,d""") should equal(Vector("a", "b, c", "d"))
    split("""a ," b,  c " ,d""") should equal(Vector("a", " b,  c ", "d"))
  }

  it should "allow to customize a maximumun number of splitted cells, remaining others unchanged" in {
    split("1, 2, 3") should equal(Vector("1", "2", "3"))
    split("1, 2, 3", maxcount = -1) should equal(Vector("1", "2", "3"))
    split("1, 2, 3", maxcount = 0) should equal(Vector("1", "2", "3"))
    split("1, 2, 3", maxcount = 1) should equal(Vector("1, 2, 3"))
    split("1, 2, 3", maxcount = 2) should equal(Vector("1", "2, 3"))
    split("1, 2, 3", maxcount = 3) should equal(Vector("1", "2", "3"))
  }

  it should "support custom cell separator" in {
    split(" 0,1; 0,2 ; 3", sep=';') should equal(Vector("0,1", "0,2", "3"))
  }

  it should "manage border line cases" in {
    split("truc, 'machin") should equal(Vector("truc", "machin"))
    split("""truc, "'machin""") should equal(Vector("truc", "'machin"))
    split("1, l'espoir, 2") should equal(Vector("1", "l'espoir", "2"))
  }
}
