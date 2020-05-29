val scalaV = "2.12.10"
//val scalaV = "2.11.8"

val projectName = "noron"
val projectVersion = "2020.05.20"

resolvers += Resolver.sonatypeRepo("snapshots")


val projectMainClass = "org.ncgw.noron.Boot"

def commonSettings = Seq(
  version := projectVersion,
  scalaVersion := scalaV,
  scalacOptions ++= Seq(
    //"-deprecation",
    "-feature"
  ),
  javacOptions ++= Seq("-encoding", "UTF-8")
)

// shadow sbt-scalajs' crossProject and CrossType until Scala.js 1.0.0 is released
import sbtcrossproject.CrossPlugin.autoImport.{crossProject, CrossType}


lazy val shared =
  crossProject(JSPlatform, JVMPlatform)
    .crossType(CrossType.Pure)
    .settings(commonSettings: _*)

lazy val sharedJvm = shared.jvm
lazy val sharedJs = shared.js

// Scala-Js frontend
lazy val frontend = (project in file("frontend"))
  .enablePlugins(ScalaJSPlugin)
  .settings(name := "frontend")
  .settings(commonSettings: _*)
  .settings(
    inConfig(Compile)(
      Seq(
        fullOptJS,
        fastOptJS,
        packageJSDependencies,
        packageMinifiedJSDependencies
      ).map(f => (crossTarget in f) ~= (_ / "sjsout"))
    ))
  .settings(skip in packageJSDependencies := false)
  .settings(
    scalaJSUseMainModuleInitializer := false,
    mainClass := Some("org.ncgw.noron.front.Main"),
    libraryDependencies ++= Seq(
      "io.circe" %%% "circe-core" % Dependencies.circeVersion,
      "io.circe" %%% "circe-generic" % Dependencies.circeVersion,
      "io.circe" %%% "circe-parser" % Dependencies.circeVersion,
      "org.scala-js" %%% "scalajs-dom" % Dependencies.scalaJsDomV,
      //"io.suzaku" %%% "diode" % "1.1.2",
      "com.lihaoyi" %%% "scalatags" % "0.6.7" withSources(),
      // "com.github.japgolly.scalacss" %%% "core" % "0.5.5" withSources(),
      "in.nvilla" %%% "monadic-html" % "0.4.0" withSources()
    )
  )
  .dependsOn(sharedJs)


// Akka Http based backend
lazy val backend = (project in file("backend")).enablePlugins(PackPlugin)
  .settings(commonSettings: _*)
  .settings(
    mainClass in reStart := Some(projectMainClass),
    javaOptions in reStart += "-Xmx2g"
  )
  .settings(name := "backend")
  .settings(
    //pack
    // If you need to specify main classes manually, use packSettings and packMain
    //packSettings,
    // [Optional] Creating `hello` command that calls org.mydomain.Hello#main(Array[String])
    packMain := Map("noron" -> projectMainClass),
    packJvmOpts := Map("noron" -> Seq("-Xmx4096m", "-Xms4096m")),
    packExtraClasspath := Map("noron" -> Seq("."))
  )
  .settings(
    libraryDependencies ++= Dependencies.backendDependencies
  )
    .settings {
      (resourceGenerators in Compile) += Def.task {
        val fastJsOut = (fastOptJS in Compile in frontend).value.data
        val fastJsSourceMap = fastJsOut.getParentFile / (fastJsOut.getName + ".map")
        Seq(
          fastJsOut,
          fastJsSourceMap
        )
      }.taskValue
    }
    .settings((resourceGenerators in Compile) += Def.task {
      Seq(
        (packageJSDependencies in Compile in frontend).value
        //(packageMinifiedJSDependencies in Compile in frontend).value
      )
    }.taskValue)
    .settings(
      (resourceDirectories in Compile) += (crossTarget in frontend).value,
      watchSources ++= (watchSources in frontend).value
    )
  .dependsOn(sharedJvm)


lazy val root = (project in file("."))
  .aggregate(frontend, backend)
  .settings(name := projectName)