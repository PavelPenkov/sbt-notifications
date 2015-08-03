sbtPlugin := true

//Change to your organization
organization := "me.penkov"

//Change to your plugin name
name := """sbt-notifications"""

//Change to the version
version := "0.0.1"

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

// Change this to another test framework if you prefer
libraryDependencies += "org.scalatest" %% "scalatest" % "2.2.4" % "test"


// Scripted - sbt plugin tests
scriptedSettings

scriptedLaunchOpts += "-Dproject.version=" + version.value