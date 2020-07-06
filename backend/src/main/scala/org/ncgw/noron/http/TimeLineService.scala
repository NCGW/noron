package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.ncgw.noron.shared.SuccessRsp
import org.slf4j.LoggerFactory
import org.ncgw.noron.Boot.executor
import org.ncgw.noron.shared.TimeLineProtocol.{TimeLineItem, TodayTasksRsp}
import org.ncgw.noron.utils.PRIUtil.schedule_today
import org.ncgw.noron.utils.CirceSupport

/**
  * Author: wmy
  * Date: 2020/06/30
  * Desc: 时间轴相关的后台接口
  */
trait TimeLineService extends ServiceUtils with CirceSupport {

  import io.circe._
  import io.circe.generic.auto._

  private val log = LoggerFactory.getLogger(getClass)

  private val getTodayTasks = (pathEndOrSingleSlash & get){
//    val fakeData = List(TimeLineItem(0, 1593425053417L, 1593425053417L, "read", None),
//      TimeLineItem(1, 1593425053417L, 1593425053417L, "write123456667782344556667", None),
//      TimeLineItem(3, 1593425053417L, 1593425053417L, "rwalking", None))
    val fakeData = schedule_today
    complete(TodayTasksRsp(fakeData))
  }

  val timeLineRoute:Route = pathPrefix("timeLine"){
    getTodayTasks
  }

}
