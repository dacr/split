name := "split"
organization :="fr.janalyse"
homepage := Some(new URL("https://github.com/dacr/split"))
licenses += "Apache 2" -> url(s"http://www.apache.org/licenses/LICENSE-2.0.txt")
scmInfo := Some(ScmInfo(url(s"https://github.com/dacr/split"), s"git@github.com:dacr/split.git"))

scalaVersion := "3.0.0"
scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.11.12", "2.12.13", "2.13.6", "3.0.0")
// 2.10.x  : generates java 6 bytecodes
// 2.11.x : generates java 6 bytecodes
// 2.12.x : generates java 8 bytecodes && JVM8 required for compilation
// 2.13.x  : generates java 8 bytecodes && JVM8 required for compilation


libraryDependencies ++= Seq(
  "org.scala-lang.modules"  %% "scala-collection-compat" % "2.4.4",
  "org.scalatest" %% "scalatest" % "3.2.9" % "test"
)

testOptions in Test += {
  val rel = scalaVersion.value.split("[.]").take(2).mkString(".")
  Tests.Argument(
    "-oDF", // -oW to remove colors
    "-u", s"target/junitresults/scala-$rel/"
  )
}
