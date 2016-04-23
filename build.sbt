name := "split"

version := "0.3.2-SNAPSHOT"

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-deprecation" )

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.10.6", "2.11.8")

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.2.6" % "test"
)

organization :="fr.janalyse"

organizationHomepage := Some(new URL("http://www.janalyse.fr"))


parallelExecution in Test := false
logBuffered in Test := false
fork in Test := true

initialCommands in console := """
   |import fr.janalyse.split._
   |import StringSplit._
   |import FlowGroup._
   |""".stripMargin

publishTo := Some(
     Resolver.sftp(
         "JAnalyse Repository",
         "www.janalyse.fr",
         "/home/tomcat/webapps-janalyse/repository"
     ) as("tomcat", new File(util.Properties.userHome+"/.ssh/id_rsa"))
)

