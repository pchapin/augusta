
Libraries
=========

This folder contains third party libraries used by Augusta that are *not* automatically
downloaded and managed by SBT. Any jar file deposited here should be picked up by SBT and
included in the classpath of the Augusta project (see the `build.sbt` file for more
information on how to set that up).

The following libraries need to be downloaded manually and put into this folder:

+ The [DocBook v5.1
  Schema](http://docs.oasis-open.org/docbook/docbook/v5.1/os/schemas/rng/docbookxi.rnc). This
  schema includes XInclude support (which is why there is an 'i' in the name). DocBook is used
  in the Scala tutorial. This is only needed if you plan to edit that tutorial using Visual
  Studio Code.