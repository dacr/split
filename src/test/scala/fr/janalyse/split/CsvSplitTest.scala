/*
 * Copyright 2011-2023 David Crosson, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package fr.janalyse.split

import org.scalatest.flatspec._
import org.scalatest.matchers._

class CsvSplitTest extends AnyFlatSpec with should.Matchers {
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
