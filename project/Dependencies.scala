import sbt._

/**
  * User: Taoz
  * Date: 6/13/2017
  * Time: 9:38 PM
  */
object Dependencies {




  val slickV = "3.3.0"
  val akkaV = "2.5.27"
  val akkaHttpV = "10.1.11"

  val circeVersion = "0.12.3"

  val scalaJsDomV = "0.9.7"

  //val scalaTagsV = "0.6.7"
  //val diodeV = "1.1.2"

  val poiV = "4.1.1"

  val akkaSeq = Seq(
    "com.typesafe.akka" %% "akka-actor" % akkaV withSources (),
    //"com.typesafe.akka" %% "akka-typed" % akkaV withSources (),
    "com.typesafe.akka" %% "akka-actor-typed" % akkaV withSources (),
    "com.typesafe.akka" %% "akka-slf4j" % akkaV,
    "com.typesafe.akka" %% "akka-stream" % akkaV
  )

  val akkaHttpSeq = Seq(
    "com.typesafe.akka" %% "akka-http-core" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http" % akkaHttpV,
    "com.typesafe.akka" %% "akka-http-testkit" % akkaHttpV
  )

  val circeSeq = Seq(
    "io.circe" %% "circe-core" % circeVersion,
    "io.circe" %% "circe-generic" % circeVersion,
    "io.circe" %% "circe-parser" % circeVersion
  )
  
  val poiSeq = Seq(
    "org.apache.poi" % "poi" % poiV,
    "org.apache.poi" % "poi-ooxml" % poiV
  )

  val scalaXml = "org.scala-lang.modules" %% "scala-xml" % "1.0.6"
  val slick = "com.typesafe.slick" %% "slick" % slickV
  val slickCodeGen = "com.typesafe.slick" %% "slick-codegen" % slickV
  val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "2.22.0"
  val hikariCP = "com.zaxxer" % "HikariCP" % "3.4.1"
  val logback = "ch.qos.logback" % "logback-classic" % "1.2.3"
  val codec = "commons-codec" % "commons-codec" % "1.13"
  val postgresql = "org.postgresql" % "postgresql" % "9.4.1208"
  val asynchttpclient = "org.asynchttpclient" % "async-http-client" % "2.10.4"
  val ehcache = "net.sf.ehcache" % "ehcache" % "2.10.6"
  val h2Database = "com.h2database" % "h2" % "1.4.200"
  val ant =  "ant" % "ant" % "1.6.5"



  val backendDependencies =
    Dependencies.akkaSeq ++
    Dependencies.akkaHttpSeq ++
    Dependencies.circeSeq ++
    Dependencies.poiSeq ++
    Seq(
      Dependencies.scalaXml,
      Dependencies.slick,
      Dependencies.slickCodeGen,
      Dependencies.nscalaTime,
      Dependencies.hikariCP,
      Dependencies.logback,
      Dependencies.codec,
//      Dependencies.postgresql,
      Dependencies.asynchttpclient,
      Dependencies.ehcache,
      Dependencies.h2Database,
      Dependencies.ant
    )



}
