package me.penkov.sbt

import sbt._
import Keys._
import complete.DefaultParsers._

object NotificationsPlugin extends AutoPlugin {

  lazy val defaultNotifier: Notifier = {
    Seq(
      ToastNotifier,
      LibNotifier,
      MacNotifier
    )
    .find(_.isAvailable)
    .getOrElse(NullNotifier)
  }

  val defaultScopeMask: ScopeMask = new ScopeMask(
    project = false,
    config = true,
    task = false,
    extra = false
  )

  /** Formats [[TaskKey]] and its [[Scope]] with the given [[ScopeMask]] */
  def formatScopedKey(
    taskKey: TaskKey[_],
    scopeMask: ScopeMask
  ): String =
    Scope.displayMasked(
      taskKey.scope,
      taskKey.key.label,
      scopeMask
    )

  def defaultSuccessFormatter(taskKey: TaskKey[_]): String =
    s"✅ ${formatScopedKey(taskKey, defaultScopeMask)}"
  def defaultFailureFormatter(taskKey: TaskKey[_]): String =
    s"⚠️ ${formatScopedKey(taskKey, defaultScopeMask)}"

  // TODO: move it somewhere
  /** Runs the task and depending on result shows success or failure notification */
  def taskWithNotification[T](
    taskKey: TaskKey[T],
    successFormatter: TaskKey[_] => String,
    failureFormatter: TaskKey[_] => String
  ): Def.Initialize[Task[T]] = taskKey.result.map {
    // Success
    case Value(value) => {
      defaultNotifier.notify(
        successFormatter(taskKey),
        value.toString
      )
      value
    }
    // Failure
    case Inc(incomplete) => {
      defaultNotifier.notify(
        failureFormatter(taskKey),
        incomplete.causes
          .flatMap(_.directCause)
          .mkString("• ", "\n• ", "")
      )
      throw incomplete
    }
  }

  object autoImport {
    val notifyFailureOnly = settingKey[Boolean]("Send notifications only if tests failed")

    /** Wraps given task into a task with notification */
    def notifyOn[T](
      taskKey: TaskKey[T],
      successFormatter: TaskKey[_] => String,
      failureFormatter: TaskKey[_] => String
    ): Setting[Task[T]] = {
      taskKey := taskWithNotification(taskKey, successFormatter, failureFormatter).value
    }

    /** Same `notifyOn` but with default formatters */
    def notifyOn[T](taskKey: TaskKey[T]): Setting[Task[T]] =
      notifyOn(taskKey, defaultSuccessFormatter, defaultFailureFormatter)
  }
  import autoImport._

  override lazy val projectSettings = Seq(
    testListeners += new NotifierTestListener(name.value, notifyFailureOnly.value),
    notifyFailureOnly := false
  )

  override def trigger = AllRequirements
}
