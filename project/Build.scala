import sbt._
import Keys._
import sbtappengine.Plugin.appengineSettings
import com.typesafe.sbt.SbtScalariform.scalariformSettings
import twirl.sbt.TwirlPlugin._

object sharenotesBuild  extends Build {

  lazy val basicSettings = seq(
    version               := "1.0.0",
    organization          := "sharenotes",
    description           := "",
    scalaVersion          := "2.10.1",
    scalacOptions         := Seq("-deprecation", "-encoding", "utf8")
  )


  // configure prompt to show current project
  override lazy val settings = super.settings ++ basicSettings :+ {
    shellPrompt := { s => Project.extract(s).currentProject.id + " > " }
  }

  lazy val root = Project(id="sharenotes", base=file("."))
    .settings(
      resolvers ++= Seq(
        "objectivy repo" at "http://maven.objectify-appengine.googlecode.com/git/")
      )
    .settings(
      libraryDependencies ++= Seq(
        "com.googlecode.objectify" % "objectify" % "4.0b1",
        "net.databinder" %% "unfiltered-filter" % "0.6.7",
        "javax.persistence" % "persistence-api" % "1.0",
        "org.skife.com.typesafe.config" % "typesafe-config" % "0.3.0",
        "com.google.appengine" % "appengine-api-1.0-sdk" % "1.6.5",
        "ch.qos.logback" % "logback-classic" % "0.9.26",
        "io.spray" %%  "spray-json" % "1.2.3",
        "commons-codec" %  "commons-codec" % "1.6",
        "org.scalatest" %% "scalatest" % "1.9.1" % "test",
        "javax.servlet" % "servlet-api" % "2.5" % "provided",
        "org.eclipse.jetty" % "jetty-webapp" % "8.1.8.v20121106" % "container")
    )
    .settings(appengineSettings: _*)
    .settings(scalariformSettings: _*)
    .settings(Twirl.settings: _*)
    .settings(
      Twirl.twirlImports := Seq("sharenotes.model._")
    )
}
