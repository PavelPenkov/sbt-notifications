val pluginVersion = Option(System.getProperty("plugin.version")).getOrElse {
  throw new RuntimeException("Add -Dplugin.version to scriptedLaunchOpts")
}
addSbtPlugin("me.penkov" % "sbt-notifications" % pluginVersion)
