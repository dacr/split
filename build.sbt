name := "split"
version := "0.3.3"

organization :="fr.janalyse"
organizationHomepage := Some(new URL("https://github.com/dacr/split"))

scalaVersion := "2.11.8"

scalacOptions ++= Seq("-unchecked", "-deprecation" )
scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.10.6", "2.11.8", "2.12.1")

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.1" % "test"



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

