package me.penkov.sbt

import sbt._
import Keys._
import complete.DefaultParsers._

object NotificationsPlugin extends AutoPlugin {
  object autoImport {
  	val notifyFailureOnly = settingKey[Boolean]("Send notifications only if tests failed")
   }

  import autoImport._ 

  override lazy val projectSettings = Seq(
    testListeners += new NotifierTestListener(name.value, notifyFailureOnly.value),
    notifyFailureOnly := false
  )
}
