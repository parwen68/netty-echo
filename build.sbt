name := """echo"""

version := "1.0"

scalaVersion := "2.10.2"

libraryDependencies ++= Seq("org.scalatest" %% "scalatest" % "1.9.1" % "test",
                            "io.netty" % "netty-all" % "4.0.9.Final")
