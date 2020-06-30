package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import org.ncgw.noron.shared.SuccessRsp
import org.slf4j.LoggerFactory
import org.ncgw.noron.Boot.executor
import org.ncgw.noron.models.dao.TaskDao
import org.ncgw.noron.shared.TaskListProtocol._
import org.ncgw.noron.utils.CirceSupport

/**
  * Author: wmy
  * Date: 2020/06/30
  * Desc: 任务列表后台接口
  */

trait TaskListService extends ServiceUtils with CirceSupport{

  import io.circe._
  import io.circe.generic.auto._

  private val log = LoggerFactory.getLogger(getClass)

  def genImg(src: String):Option[String] = {
    if(src == "") None else Some(src)
  }

  private val getTaskList= (path("/") & get){
    parameter('id.as[Int]){uId =>
      dealFutureResult{
        TaskDao.getTaskList(uId).map{lst =>
          val taskList = lst.map(r =>
            TaskItem(r._1, r._2.getOrElse(0), r._3.getOrElse(0), r._4.getOrElse("暂无内容"), r._5, r._6, r._7, r._8)).toList
          complete(TaskListRsp(taskList))
        }
      }
    }
  }



  val route:Route = pathPrefix("timeLine"){
    getTaskList
  }

}
