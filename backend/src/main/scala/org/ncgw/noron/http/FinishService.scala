package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.norn.shared.TaskStartProtocol._
import org.ncgw.norn.shared.CommonRsp
import org.ncgw.noron.models.dao.InfoDAO
import org.ncgw.noron.protocols.{SuccessRsp, parseError,ErrorRsp}

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
  private val getTaskInfo = (path("getTaskInfo") & post) {
    entity(as[Either[Error, GetTaskInfoReq]]) {
      case Left(error) =>
        println("okkkkkkkkkkkkkkk1")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        println("okkkkkkkkkkkkkkk2")
        dealFutureResult(
          InfoDAO.getInfoByTaskid(req.taskid).map{info=>
            if(info.isEmpty){
              complete(ErrorRsp(100102,"task is not exist"))
            }else{
              complete(GetTaskInfoRsp(info))
            }
          }
        )
    }
  }

  //获取日程信息
  private val updateProgress=(path("updateProgress") & post) {
    entity(as[Either[Error, GetTaskInfoReq]]) {
      case Left(error) =>
        println("okkkkkkkkkkkkkkk1")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        println("okkkkkkkkkkkkkkk2")
        dealFutureResult(
          InfoDAO.getInfoByTaskid(req.taskid).map{info=>
            if(info.isEmpty){
              complete(ErrorRsp(100102,"task is not exist"))
            }else{
              complete(GetTaskInfoRsp(info))
            }
          }
        )
    }
  }

  //获取日程信息
  private val updateReward=(path("updateReward") & post) {
    entity(as[Either[Error, GetTaskInfoReq]]) {
      case Left(error) =>
        println("okkkkkkkkkkkkkkk1")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
        println("okkkkkkkkkkkkkkk2")
        dealFutureResult(
          InfoDAO.getInfoByTaskid(req.taskid).map{info=>
            if(info.isEmpty){
              complete(ErrorRsp(100102,"task is not exist"))
            }else{
              complete(GetTaskInfoRsp(info))
            }
          }
        )
    }
  }


  val finish: Route =
    pathPrefix("finishContent") {
      getTaskInfo ~ updateProgress ~ updateReward
    }

}
