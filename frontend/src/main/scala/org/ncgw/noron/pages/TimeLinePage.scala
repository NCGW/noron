package org.ncgw.noron.pages

import mhtml.{Rx, Var, emptyHTML}
import org.ncgw.noron.shared.TimeLineProtocol._
import org.ncgw.noron.Routes
import org.ncgw.noron.utils.{Http, TimeTool}

import concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import io.circe.syntax._

import scala.scalajs.js.Date
import scala.xml.{Elem, Node}

/**
  * Author: wmy
  * Date: 2020/06/25
  * Desc: 时间轴页面
  */

object TimeLinePage {

  val fakeData = List(TimeLineItem(0, 1593425053417L, 1593425053417L, "read", None),
    TimeLineItem(1, 1593425053417L, 1593425053417L, "write123456667782344556667", None),
    TimeLineItem(3, 1593425053417L, 1593425053417L, "rwalking", None))
  val todayTasks = Var(fakeData)
  val showDetail = Var(-1)

  def getTodayTasks():Unit = {
    Http.getAndParse[TodayTasksRsp](Routes.TimeLine.getTodayTasks).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          todayTasks := rsp.todayTasks
        }else{
          println("error, ", rsp.msg)
        }

      case Left(e) =>
        println("error, parse json error", e)
    }
  }

  def createTimeLineItem(item: TimeLineItem, idx: Int):Elem = {
    val detailStyle = showDetail.map{i => if(i == item.taskId) "display:block" else "display:none"}
    <div class="tlp-item">
      <div class="tlp-detail" style={detailStyle}>
        <p class="tlp-detail-p">{item.content}</p>
        {
        if(item.img.isDefined)
          <img src={item.img.get}></img>
        else
          emptyHTML
        }
      </div>
      {
        val class4item = s"tlp-simple tlp-color$idx "
        <div class={class4item} onclick={() => showDetail := item.taskId}>
          <p class="tlp-simple-time">
            {TimeTool.DateFormatter(new Date(item.startTime), "hh:mm")}-{TimeTool.DateFormatter(new Date(item.endTime), "hh:mm")}
          </p>
          <p class="tlp-simple-p">{item.content}</p>
        </div>
      }
    </div>
  }

  val timeLine:Elem =
    <div class="tlp-wrapper">
      {todayTasks.map{ lst =>
      if(lst.isEmpty){
        emptyHTML
      }else{
        <div>
          {lst.zipWithIndex.map{l => createTimeLineItem(l._1, l._2 % 3)}}
        </div>
      }}}
    </div>

  def app:Node = {
//    getTodayTasks()
    timeLine
  }

}
