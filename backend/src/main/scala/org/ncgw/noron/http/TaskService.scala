package org.ncgw.noron.http

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix}
import org.ncgw.noron.shared.InputProtocol._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
import org.ncgw.noron.models.dao.{TaskDao}

import scala.concurrent.ExecutionContext.Implicits.global

trait TaskService extends ServiceUtils with SessionBase{

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  private val addTask = (path("addTask") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error, AddTaskReq]]) {
      case Right(req) =>
        dealFutureResult {
          TaskDao.addTask(req.userId, req.startTime, req.content, req.taskType, req.img).map { res =>
            complete(SuccessRsp())
          }.recover {
            case e: Exception =>
              log.warn(s"add task error,${e.getMessage}")
              complete(ErrorRsp(4000031, "add task error."))
          }
        }

      case Left(error) =>
        log.warn(s"some error: $error")
        complete(ErrorRsp(400101,"parse error"))
    }
  }


  val task: Route =
    pathPrefix("task") {
      addTask
    }
}
