package org.ncgw.noron.http

import akka.actor.{ActorSystem, Scheduler}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.Materializer
import akka.util.Timeout


import scala.concurrent.ExecutionContextExecutor

/**
  * User: Taoz
  * Date: 2020/3/20
  */
trait HttpService extends ResourceService
  with ServiceUtils
  with StartService
  with FinishService
  with RankService
  with TaskService
  with SpeechToTextService{

  implicit val system: ActorSystem

  implicit val executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val home = get {
    setNoCacheInHeaderWithFilter {
      getFromResource("html/index.html")
    }
  }

  lazy val routes: Route = {
    ignoreTrailingSlash {
      pathPrefix("noron"){
        pathEndOrSingleSlash{
          home
        } ~ resourceRoutes ~ Start ~ finish ~ parseText ~ rank ~ task
      }
    }
  }

}
