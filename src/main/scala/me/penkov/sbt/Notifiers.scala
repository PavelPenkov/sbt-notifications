package me.penkov.sbt

import sbt._
import java.io.ByteArrayInputStream
import java.nio.charset.StandardCharsets
import scala.xml._
import scala.sys.process.ProcessLogger

object ToastNotifier extends Notifier {
  override def isAvailable = System.getProperty("os.name").startsWith("Windows")
  
  def notify(title: String, message: String) {
    val toast = <toast duration="long">
                  <audio silent="true" />
                  <visual>
                    <binding template="ToastText02">
                      <text id="1">{title}</text>
                      <text id="2">{message}</text>
                    </binding>
                  </visual>
                </toast>

    val script = s"""
    [Windows.UI.Notifications.ToastNotificationManager, Windows.UI.Notifications, ContentType = WindowsRuntime] > $$null
    [Windows.Data.Xml.Dom.XmlDocument, Windows.Data.Xml.Dom.XmlDocument, ContentType = WindowsRuntime] > $$nul
    [Windows.UI.Notifications.ToastNotification, Windows.UI.Notifications, ContentType = WindowsRuntime] > $$null
    $$xml = New-Object Windows.Data.Xml.Dom.XmlDocument
    $$template = "${toPsString(toast)}"
    $$xml.LoadXml($$template)
    $$toast = New-Object Windows.UI.Notifications.ToastNotification $$xml
    [Windows.UI.Notifications.ToastNotificationManager]::CreateToastNotifier("sbt").Show($$toast)
    """

    val stream = new ByteArrayInputStream(script.getBytes(StandardCharsets.UTF_8))
    ("powershell -Command -" #< stream).! 
  }
  
  private def toPsString(xml: Elem) = xml.toString.split("\n").map(_.trim).mkString.replace("\"", "`\"")
}

object MacNotifier extends Notifier {
  override def isAvailable = System.getProperty("os.name").startsWith("Mac")
  
  def notify(title: String, message: String) = {
    val script = s"""display notification "$message" with title "$title" """
    (s"osascript -e '$script'").!
  }
}

object LibNotifier extends Notifier {
  override def isAvailable = {
    System.getProperty("os.name") == "Linux" && "which notify-send".!!.matches(".*notify-send\\s+")
  }

  def notify(title: String, message: String) = ("notify-send" +: Seq(title, message)).!
}