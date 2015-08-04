# sbt-notifications

A simple sbt 0.13.x plugin that sends native OS notifications when tests are completed. Uses Toast notifications on Windows (tested on Windows 10), Notification Center on Mac OS X (tested on Mac OS X Yosemite) and `libnotify` on Linux (requires `libnotify-bin` package, tested on Ubuntu with Gnome).

### Usage
Add the following to `./project/plugins.sbt`
    
	addSbtPlugin("me.penkov" % "sbt-notifications" % "0.0.2")

To receive notifications only when tests failed use 

	notifyOnlyFailure := true

in build definition.