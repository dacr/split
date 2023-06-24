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

import org.scalatest.funsuite._
import org.scalatest.matchers._
import org.scalatest.OptionValues._

import java.util.regex.Pattern

class KeyValSplitTest extends AnyFunSuite with should.Matchers{
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