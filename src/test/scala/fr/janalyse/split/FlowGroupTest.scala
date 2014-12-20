package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers

class FlowGroupTest extends FunSuite with ShouldMatchers {

  import FlowGroup._

  test("basic") {
    val in = Stream(
      "-", "A", "B", "-", "A", "B", "C", "-", "Z")
    restrings(in, _ == "-", "") should equal(List("-AB", "-ABC", "-Z"))
  }

  test("perfs") {
    def data =
      """avr. 09, 2014 9:35:41 PM org.apache.catalina.startup.Catalina start
        |Infos: Server startup in 5499 ms
        |avr. 09, 2014 9:35:41 PM org.apache.catalina.core.ApplicationContext log
        |Infos: ContextListener: attributeAdded('org.apache.jasper.compiler.TldLocationsCache', 'org.apache.jasper.compiler.TldLocationsCache@27871523')
        |avr. 09, 2014 9:36:19 PM org.apache.catalina.core.StandardWrapperValve invoke
        |Grave: Servlet.service() for servlet [fr.janalyse.primesui.PrimesServlet] in context with path [/primesui] threw exception [L''exécution de la servlet a lancé une exception] with root cause
        |com.mchange.v2.resourcepool.CannotAcquireResourceException: A ResourcePool could not acquire a resource from its primary factory or source.
        |        at com.mchange.v2.resourcepool.BasicResourcePool.awaitAvailable(BasicResourcePool.java:1418)
        |        at com.mchange.v2.resourcepool.BasicResourcePool.prelimCheckoutResource(BasicResourcePool.java:606)
        |        at com.mchange.v2.resourcepool.BasicResourcePool.checkoutResource(BasicResourcePool.java:526)
        |        at com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.checkoutAndMarkConnectionInUse(C3P0PooledConnectionPool.java:756)
        |        at com.mchange.v2.c3p0.impl.C3P0PooledConnectionPool.checkoutPooledConnection(C3P0PooledConnectionPool.java:683)
        |        at com.mchange.v2.c3p0.impl.AbstractPoolBackedDataSource.getConnection(AbstractPoolBackedDataSource.java:140)
        |        at fr.janalyse.primesui.PrimesDBInit$class.connection$1(PrimesDB.scala:118)
        |        at fr.janalyse.primesui.PrimesDBInit$$anonfun$dbSetup$1.apply(PrimesDB.scala:119)
        |        at fr.janalyse.primesui.PrimesDBInit$$anonfun$dbSetup$1.apply(PrimesDB.scala:119)
        |        at org.squeryl.SessionFactory$.newSession(Session.scala:95)
        |        at org.squeryl.dsl.QueryDsl$class.transaction(QueryDsl.scala:78)
        |        at org.squeryl.PrimitiveTypeMode$.transaction(PrimitiveTypeMode.scala:40)
        |        at fr.janalyse.primesui.PrimesEngine$$anonfun$valuesCount$1.apply$mcJ$sp(PrimesEngine.scala:105)
        |        at fr.janalyse.primesui.PrimesEngine$$anonfun$valuesCount$1.apply(PrimesEngine.scala:105)
        |        at fr.janalyse.primesui.PrimesEngine$$anonfun$valuesCount$1.apply(PrimesEngine.scala:105)
        |        at fr.janalyse.primesui.PrimesEngine.usingcache(PrimesEngine.scala:93)
        |        at fr.janalyse.primesui.PrimesEngine.valuesCount(PrimesEngine.scala:105)
        |        at fr.janalyse.primesui.PrimesServlet$$anonfun$13.apply(PrimesServlet.scala:27)
        |        at fr.janalyse.primesui.PrimesServlet$$anonfun$13.apply(PrimesServlet.scala:22)
        |avr. 09, 2014 11:08:24 PM org.apache.catalina.core.ApplicationContext log
        |Infos: SessionListener: contextDestroyed()
        |avr. 09, 2014 11:08:24 PM org.apache.catalina.core.ApplicationContext log
        |Infos: ContextListener: contextDestroyed()
        |""".stripMargin.split("\n").toStream
    def now = System.currentTimeMillis()
    val started = now
    var processedEntries = 0L
    var processedLines=0L
    case class LogEntry(content: List[String])
    val logStartRE = """^\w+[.] \d{2}, \d{4} \d{1,2}:\d{2}:\d{2} [AP]M """.r
    def logStartTest(l: String): Option[String] = {
      if (logStartRE.findFirstIn(l).isDefined) Some(l) else None
    }
    do {
      val logslines=data
      val entries = reassemble(logslines, logStartTest, (l: String, r: List[String]) => LogEntry(l :: r))
      processedEntries += entries.size
      processedLines += logslines.size
    } while (now - started < 10000L)
    val howlong = (now - started)/1000
    info(f"found ${processedEntries/howlong}%,d entries/second through ${processedLines/howlong}%,d lines/second")
  }
}
