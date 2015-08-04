sbtPlugin := true

organization := "me.penkov"

name := """sbt-notifications"""

version := "0.0.2"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

scalaVersion := "2.10.5"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

scriptedSettings

scriptedLaunchOpts += "-Dproject.version=" + version.value
