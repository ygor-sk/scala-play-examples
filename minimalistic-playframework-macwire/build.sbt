name := """scalactica2d"""
organization := "sk.ygor"
version := "1.0-SNAPSHOT"
scalaVersion := "2.12.6" // use latest

lazy val root = (project in file(".")).enablePlugins(PlayScala)

libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.1" % "provided"
libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.1"

libraryDependencies += "commons-dbcp" % "commons-dbcp" % "1.4"