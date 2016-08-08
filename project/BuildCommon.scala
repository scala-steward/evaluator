import org.scalafmt.sbt.ScalaFmtPlugin
import org.scalafmt.sbt.ScalaFmtPlugin.autoImport._
import sbt.Keys._
import sbt._

object BuildCommon extends AutoPlugin {

  override def requires = plugins.JvmPlugin && ScalaFmtPlugin

  override def trigger = allRequirements

  object autoImport {

    val libraryVersions = settingKey[Map[Symbol, String]]("Common versions to be used for dependencies")

    def compilerDependencySettings = Seq(
      libraryDependencies ++= Seq(
        "org.scala-lang" % "scala-compiler" % scalaVersion.value,
        compilerPlugin(
          "org.scalamacros" % "paradise" % "2.1.0" cross CrossVersion.full
        )
      )
    )
  }

  import autoImport._

  override def projectSettings =
    baseSettings ++
      reformatOnCompileSettings ++
      dependencySettings ++
      miscSettings


  private[this] def baseSettings = Seq(
    version := "0.0.1-SNAPSHOT",
    organization := "org.scala-exercises",
    scalaVersion := "2.11.8",
    scalafmtConfig in ThisBuild := Some(file(".scalafmt")),

    resolvers ++= Seq(Resolver.mavenLocal, Resolver.sonatypeRepo("snapshots"), Resolver.sonatypeRepo("releases")),

    parallelExecution in Test := false,
    cancelable in Global := true,

    scalacOptions ++= Seq(
      "-deprecation", "-feature", "-unchecked", "-encoding", "utf8"),
    scalacOptions ++= Seq(
      "-language:implicitConversions",
      "-language:higherKinds"),
    javacOptions ++= Seq("-encoding", "UTF-8", "-Xlint:-options")
  )

  private[this] def dependencySettings = Seq(
    libraryVersions := Map(
      'cats -> "0.6.1",
      'circe -> "0.5.0-M2",
      'config -> "1.3.0",
      'coursier -> "1.0.0-M12",
      'http4s -> "0.14.1",
      'jwtcore -> "0.8.0",
      'log4s -> "1.3.0",
      'monix -> "2.0-RC8",
      'scalajhttp -> "2.3.0",
      'scalacheck -> "1.12.5",
      'scalaTest -> "2.2.6",
      'slf4j -> "1.7.21"
    )
  )

  private[this] def miscSettings = Seq(
    shellPrompt := { s: State =>
      val c = scala.Console
      val blue = c.RESET + c.BLUE + c.BOLD
      val white = c.RESET + c.BOLD

      val projectName = Project.extract(s).currentProject.id

      s"$blue$projectName$white>${c.RESET}"
    }
  )
}