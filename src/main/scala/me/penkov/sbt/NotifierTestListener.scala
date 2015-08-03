package me.penkov.sbt

import sbt._
import testing._
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets

trait Notifier {
  def notify(title: String, message: String) : Unit

  def isAvailable : Boolean
}

object NullNotifier extends Notifier {
  def notify(title: String, message: String) = {}
  def isAvailable = true
}

class NotifierTestListener(val projectName: String, val onlyFailure: Boolean = false) extends TestsListener {
  private var errorCount, passedCount, ignoredCount, failedCount, pendingCount = 0

  val notifier = List(ToastNotifier).find(_.isAvailable) getOrElse NullNotifier
  
  override def doInit() = {
    errorCount = 0
    passedCount = 0
    ignoredCount = 0
    failedCount = 0    
    pendingCount = 0
  }  
  
  override def endGroup(name: String, result: TestResult.Value) = {}
  
  override def endGroup(name: String, t: Throwable) = {}
  
  override def startGroup(name: String) = {}
  
  override def testEvent(event: TestEvent) = {
    val result = SuiteResult(event.detail)
    errorCount+=result.errorCount
    passedCount+=result.passedCount
    ignoredCount+=result.ignoredCount
    failedCount+=result.failureCount
    pendingCount+=result.pendingCount
  }
  
  override def doComplete(finalResult: TestResult.Value) = {
    val message = s"Tests: passed $passedCount, failed $failedCount, ignored $ignoredCount, pending $pendingCount"    
    
    if(!onlyFailure || (onlyFailure && finalResult != TestResult.Passed)) notifier.notify(s"$projectName - $finalResult", message)
  }
}