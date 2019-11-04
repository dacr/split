name := "split"

organization :="fr.janalyse"
homepage := Some(new URL("https://github.com/dacr/split"))

scalaVersion := "2.13.1"

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.10.7", "2.11.12", "2.12.10", "2.13.1")
// 2.9.3   : generates java 5 bytecodes, even with run with a JVM6
// 2.10.7  : generates java 6 bytecodes
// 2.11.12 : generates java 6 bytecodes
// 2.12.10 : generates java 8 bytecodes && JVM8 required for compilation
// 2.13.1  : generates java 8 bytecodes && JVM8 required for compilation


libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.8" % "test"

testOptions in Test += Tests.Argument(TestFrameworks.ScalaTest, "-u", "target/junitresults")

parallelExecution in Test := false
logBuffered in Test := false
fork in Test := true

initialCommands in console := """
   |import fr.janalyse.split._
   |import StringSplit._
   |import FlowGroup._
   |""".stripMargin

pomIncludeRepository := { _ => false }

useGpg := true

licenses += "Apache 2" -> url(s"http://www.apache.org/licenses/LICENSE-2.0.txt")
releaseCrossBuild := true
releasePublishArtifactsAction := PgpKeys.publishSigned.value
publishMavenStyle := true
publishArtifact in Test := false
publishTo := Some(if (isSnapshot.value) Opts.resolver.sonatypeSnapshots else Opts.resolver.sonatypeStaging)

scmInfo := Some(ScmInfo(url(s"https://github.com/dacr/split"), s"git@github.com:dacr/split.git"))

PgpKeys.useGpg in Global := true      // workaround with pgp and sbt 1.2.x
pgpSecretRing := pgpPublicRing.value  // workaround with pgp and sbt 1.2.x

pomExtra in Global := {
  <developers>
    <developer>
      <id>dacr</id>
      <name>David Crosson</name>
      <url>https://github.com/dacr</url>
    </developer>
  </developers>
}


import ReleaseTransformations._
releaseProcess := Seq[ReleaseStep](
    checkSnapshotDependencies,
    inquireVersions,
    runClean,
    runTest,
    setReleaseVersion,
    commitReleaseVersion,
    tagRelease,
    publishArtifacts,
    setNextVersion,
    commitNextVersion,
    releaseStepCommand("sonatypeReleaseAll"),
    pushChanges
  )
 
