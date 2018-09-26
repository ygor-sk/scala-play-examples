lazy val commonSettings = Seq(
  organization := "sk.ygor",
  version := "1.0-SNAPSHOT",
  scalaVersion := "2.12.6",
  libraryDependencies += "com.softwaremill.macwire" %% "macros" % "2.3.1" % "provided",
  libraryDependencies += "com.softwaremill.macwire" %% "util" % "2.3.1",
)

lazy val root = (project in file("."))
  .settings(
    name := "scalactica2d",
    commonSettings,
  )
  .aggregate(
    scalactica2dWeb,
    scalactica2dServices,
    scalactica2dDao,
    scalactica2dUtil,
    scalactica2dMacro,
  )

lazy val scalactica2dWeb = (project in file("modules/scalactica2d-web"))
  .enablePlugins(PlayScala)
  .settings(
    name := "scalactica2d-web",
    commonSettings,
  )
  .dependsOn(scalactica2dServices)

lazy val scalactica2dServices = (project in file("modules/scalactica2d-services"))
  .settings(
    name := "scalactica2d-services",
    commonSettings,
  ).dependsOn(scalactica2dDao)

lazy val scalactica2dDao = (project in file("modules/scalactica2d-dao"))
  .settings(
    name := "scalactica2d-dao",
    commonSettings,
    libraryDependencies += "commons-dbcp" % "commons-dbcp" % "1.4"
  ).dependsOn(scalactica2dUtil)

lazy val scalactica2dUtil = (project in file("modules/scalactica2d-util"))
  .settings(
    name := "scalactica2d-util",
    commonSettings,
  ).dependsOn(scalactica2dMacro)

lazy val scalactica2dMacro = (project in file("modules/scalactica2d-macro"))
  .settings(
    name := "scalactica2d-macro",
    commonSettings,
  )

addCommandAlias("runWeb", "scalactica2dWeb/run")