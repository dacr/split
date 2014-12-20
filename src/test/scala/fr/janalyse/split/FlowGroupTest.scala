package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class FlowGroupTest extends FunSuite with ShouldMatchers {
  
  import FlowGroup._
  
  test("basic") {
    val in = Stream(
        "-", "A", "B", "-", "A", "B", "C", "-", "Z"
        )
    restrings(in, _ == "-", "") should equal(List("-AB", "-ABC", "-Z"))
  }
}
