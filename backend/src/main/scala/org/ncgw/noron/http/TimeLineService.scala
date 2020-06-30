package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.ncgw.noron.shared.SuccessRsp
import org.slf4j.LoggerFactory
import org.ncgw.noron.Boot.executor
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

  private val getTodayTasks = (path("/") & get){
    dealFutureResult{

    }
    complete(SuccessRsp(0, "0k"))
  }

  val route:Route = pathPrefix("timeLine"){
    getTodayTasks
  }

}
