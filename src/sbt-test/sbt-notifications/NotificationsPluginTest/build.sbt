name := "toast-plugin-test"

libraryDependencies ++= Seq(
  "org.scalatest" %% "scalatest" % "3.0.4" % Test,
	"org.specs2" %% "specs2-core" % "4.0.1" % Test
)

scalacOptions in Test ++= Seq("-Yrangepos")

notifyFailureOnly := true
