package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.noron.shared.TaskStartProtocol._
import org.ncgw.noron.shared.CommonRsp
import org.ncgw.noron.models.dao.InfoDAO
import org.ncgw.noron.protocols.{SuccessRsp, parseError,ErrorRsp}

import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: WYN
  * Date: 2020/6/19
  * Time: 15:41
  */
trait StartService extends ServiceUtils with SessionBase{

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)

  //获取日程信息
  private val getTaskInfo=(path("getInfo") & post) {
    entity(as[Either[Error, GetTaskInfoReq]]) {
      case Left(error) =>
//        println("okkkkkkkkkkkkkkk1")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
//        println("okkkkkkkkkkkkkkk2")
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

  private val delay=(path("delay") & post) {
    entity(as[Either[Error, DelayReq]]) {
      case Left(error) =>
//        println("okkkkkkkkkkkkkkk3")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
//        println("okkkkkkkkkkkkkkk4")
        println(req.taskid)
        dealFutureResult(
          InfoDAO.delay(req.taskid,req.mins_delay).map{r=>
            if (r > 0) {
              complete(SuccessRsp(0,req.taskid.toString+"DELAY"+req.mins_delay.toString+"MINTYES OK!"))
            } else {
              complete(ErrorRsp(1000101, "delay failed"))
            }
          }
        )
    }
  }

  private val StartorCancle=(path("startorcancel") & post) {
    entity(as[Either[Error, StartorCancleReq]]) {
      case Left(error) =>
//        println("okkkkkkkkkkkkkkk5")
        log.warn(s"some error: $error")
        complete(parseError)
      case Right(req) =>
//        println("okkkkkkkkkkkkkkk6")
        dealFutureResult(
          InfoDAO.startorCancle(req.taskid,req.type_sorc).map{r=>
            if (r > 0) {
              complete(SuccessRsp(0,req.taskid.toString+" "+req.type_sorc+" OK!"))
            } else {
              complete(ErrorRsp(1000101, "start or cancle failed"))
            }
          }
        )
    }
  }

  val Start: Route =
    pathPrefix("startContent") {
      getTaskInfo ~ delay ~ StartorCancle
    }

}
