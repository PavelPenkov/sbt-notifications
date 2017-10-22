package me.penkov.sbt

import sbt._
import Keys._
import complete.DefaultParsers._

object NotificationsPlugin extends AutoPlugin {

  // TODO: move it somewhere
  def notifyOnDef[T](t: TaskKey[T]): Def.Initialize[Task[T]] = {
    // TODO: some shortcut for getting default notifier
    val notifier = Seq(
      ToastNotifier,
      LibNotifier,
      MacNotifier
    ).find(_.isAvailable) getOrElse NullNotifier

    t.result.map {
      case Inc(incomplete) => {
        println(incomplete.toString)
        notifier.notify(
          s"⚠️ ${t.key.label}",
          incomplete.causes.flatMap(_.directCause).mkString("• ", "\n• ", "")
        )
        throw incomplete
      }
      case Value(value) => {
        println(t.scope.toString)
        notifier.notify(
          s"✅ ${t.key.label}",
          value.toString
        )
        value
      }
    }
  }

  object autoImport {
  	val notifyFailureOnly = settingKey[Boolean]("Send notifications only if tests failed")

    def notifyOn[T](t: TaskKey[T]): Setting[Task[T]] = {
      t := notifyOnDef(t).value
    }
  }
  import autoImport._

  override lazy val projectSettings = Seq(
    testListeners += new NotifierTestListener(name.value, notifyFailureOnly.value),
    notifyFailureOnly := false
  )

  override def trigger = AllRequirements
}
