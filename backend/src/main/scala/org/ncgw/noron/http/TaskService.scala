package org.ncgw.noron.http

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix}
import org.ncgw.noron.shared.InputProtocol._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
import org.ncgw.noron.models.dao.TaskDao
import org.ncgw.noron.shared.TaskListProtocol.{TaskItem, TaskListRsp}

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

  private val getTaskList = (path("all") & get){
    parameter('userId.as[Long]){uId =>
      log.debug(s"userId-$uId get taskList")
      dealFutureResult{
        TaskDao.getTaskList(uId).map{lst =>
          val taskList = lst.map(r =>
            TaskItem(r._1, r._2.getOrElse(0), r._3.getOrElse(0), r._4.getOrElse("暂无内容"), r._5, r._6, r._7, r._8)).toList
          complete(TaskListRsp(taskList))
        }.recover{
          case e:Exception =>
            log.error(s"get task list error, $e")
            complete(ErrorRsp(4000032, "get task error"))
        }
      }
    }
  }

  private val deleteTask = (path("delete") & get){
    parameter('taskId.as[Long]){tId =>
      log.debug(s"user delete task-$tId")
      dealFutureResult{
        TaskDao.deleteTask(tId).map{_ =>
          complete(SuccessRsp)
        }.recover{
          case e:Exception =>
            log.error(s"delete task error, $e")
            complete(ErrorRsp(4000033, "delete task error"))
        }
      }
    }
  }


  val task: Route =
    pathPrefix("task") {
      addTask ~ getTaskList ~ deleteTask
    }
}
