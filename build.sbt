sbtPlugin := true

organization := "me.penkov"

name := """sbt-notifications"""

version := "0.0.2"

licenses += ("MIT", url("http://opensource.org/licenses/MIT"))

sbtVersion := "1.0.2"
scalaVersion := "2.12.3"

scalacOptions ++= Seq("-deprecation", "-feature")

resolvers += Resolver.sonatypeRepo("snapshots")

scriptedBufferLog := false
scriptedLaunchOpts ++= Seq(
  "-Xmx1024M",
  s"-Dplugin.version=${version.value}"
)
