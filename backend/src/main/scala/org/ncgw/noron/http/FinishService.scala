package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.noron.shared.TaskStartProtocol._
import org.ncgw.noron.shared.FinishTaskProtocol._
import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
import org.ncgw.noron.shared.CommonRsp
import org.ncgw.noron.models.dao.{InfoDAO, UserDao}

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * User: gaohan
  * Date: 2020/6/23
  * Time: 19:13
  */
trait FinishService extends ServiceUtils with SessionBase{

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  //获取日程信息
  private val getInfo = (path("getInfo") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error, GetTaskInfoReq]]) {
      case Right(req) =>
        dealFutureResult {
          InfoDAO.getInfoByTaskid(req.taskid).map { info =>
            if (info.isEmpty) {
              complete(ErrorRsp(100102, "task is not exist"))
            } else {
              complete(GetTaskInfoRsp(info))
            }
          }.recover {
            case e: Exception =>
              log.warn(s"get info error,${e.getMessage}")
              complete(ErrorRsp(1000031, "get info error."))
          }
        }

      case Left(error) =>
        log.warn(s"some error: $error")
        complete(ErrorRsp(100101,"parse error"))
    }
  }

  //更新进度
  private val updateProgress=(path("updateProgress") & post & pathEndOrSingleSlash ) {

    entity(as[Either[Error, UpdateProgressReq]]) {
      case Right(req) =>
        dealFutureResult(
          InfoDAO.updateProgress(req.taskId, req.progress).map{info=>
              complete(SuccessRsp())

          }
        )

      case Left(error) =>
        log.warn(s"parse error: $error")
        complete(ErrorRsp(100101,"parse error"))
    }
  }

  //更新积分
  private val updateReward=(path("updateReward") & post) {
    entity(as[Either[Error, UpdateRewardReq]]) {
      case Right(req) =>
        dealFutureResult{
          UserDao.getReward(req.useId).map{ r =>
            dealFutureResult{
              UserDao.updateReward(req.useId, Some(req.reward + r.head.get)).map( s =>
                complete(SuccessRsp())
              )
            }

          }

        }



      case Left(error) =>
        log.warn(s"some error: $error")
        complete(ErrorRsp(100103,"parse error"))
    }
  }


  val finish: Route =
    pathPrefix("finishContent") {
      getInfo ~ updateProgress ~ updateReward
    }

}
