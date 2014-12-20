
name := "split"

version := "0.1"

scalaVersion := "2.10.4"

scalacOptions ++= Seq("-unchecked", "-deprecation" )

libraryDependencies += "org.scalatest" %% "scalatest" % "1.9.+" % "test"

initialCommands in console := """
import fr.janalyse.split._
import StringSplit._
import FlowGroup._
"""

