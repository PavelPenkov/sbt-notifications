import complete.DefaultParsers._

val root = (project in file(".")).enablePlugins(me.penkov.sbt.NotificationsPlugin)

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "2.2.4" % "test",
	"org.specs2" %% "specs2-core" % "3.6.4" % "test")

scalacOptions in Test ++= Seq("-Yrangepos")

name := "toast-plugin-test"

notifyFailureOnly := false