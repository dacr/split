
name := "split"

version := "0.1"

scalaVersion := "2.11.4"

scalacOptions ++= Seq("-unchecked", "-deprecation" )

scalacOptions ++= Seq( "-deprecation", "-unchecked", "-feature")

crossScalaVersions := Seq("2.10.4", "2.11.4")

libraryDependencies ++= Seq(
    "org.scalatest" %% "scalatest" % "2.1.5" % "test"
)

initialCommands in console := """
import fr.janalyse.split._
import StringSplit._
import FlowGroup._
"""

publishTo := Some(
     Resolver.sftp(
         "JAnalyse Repository",
         "www.janalyse.fr",
         "/home/tomcat/webapps-janalyse/repository"
     ) as("tomcat", new File(util.Properties.userHome+"/.ssh/id_rsa"))
)

