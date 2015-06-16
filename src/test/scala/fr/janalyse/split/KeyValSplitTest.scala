package fr.janalyse.split

import org.scalatest.FunSuite
import org.scalatest.ShouldMatchers
import java.util.regex.Pattern
import org.scalatest.OptionValues._

class KeyValSplitTest extends FunSuite with ShouldMatchers {
  import KeyValSplit._
  
  test("keyvals split") {
    val input =
      """format=java,
        | viewer=applet,
        | context=/index.jsp,
        | http_server=http://www.google.fr,
        | page=1,
        | mysession=AAFAFAFAFAFAFAFAFAFAFAFAFAFAFAFF,
        | emrcode=codeX,
        | timezone=GMT,
        | application_context=/truc,
        | content=application/emeraude,
        | locale=fr,
        | report=file:/toto.rpt,
        | cmd=get_ab,
        | dll=bdd.dll,
        | paramt6='yyyy/MM/dd',
        | clientadress=192.168.2.1, 192.168.2.2,
        | paramt2=provider : default, customer = mycust,
        | cookie=TRUC=fafafafafafafafa; MUCHED=fafafafafafafafa,
        | paramt1=Nov 16, 1974,
        | paramt0=Nov 14, 1974
        """.stripMargin.replaceAll(Pattern.quote("\n"), "")
    val res = keyValSplit(input)
    res should have size (20)
    res.keys should contain allOf (
      "format", "viewer", "context", "http_server",
      "page", "mysession",
      "emrcode", "timezone", "application_context", "content",
      "locale", "report", "cmd", "dll", "paramt6", "clientadress",
      "paramt2", "cookie", "paramt1", "paramt0"
    )
    res.get("clientadress").value should equal("192.168.2.1, 192.168.2.2")
  }

}