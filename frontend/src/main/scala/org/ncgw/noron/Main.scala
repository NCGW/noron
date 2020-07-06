package org.ncgw.noron

import mhtml.{mount, _}
import org.ncgw.noron.common.Constant
import org.ncgw.noron.pages.{InputPage, RankPage}
import org.ncgw.noron.pages.FinishPage
import org.ncgw.noron.pages.StartPage
import org.ncgw.noron.pages._
import org.scalajs.dom
import org.ncgw.noron.utils.PageSwitcher



/**
  * created by wmy on 2019/12/05
  *
  */
object Main extends PageSwitcher {

  private val currentPage = currentHashVar.map { ls =>
    println(s"currentPage change to ${ls.mkString(",")}")

    ls match {
      case "start":: taskid :: Nil=>
        new StartPage(taskid).app
      case "finish":: taskId :: Nil=>
        new FinishPage(taskId).app
      case "rank":: userId :: Nil=>
        new RankPage(userId.toLong).app

      case "timeLine" :: Nil =>
        TimeLinePage.app

      case "taskList" :: Nil =>
        TaskList.app

      case "input" :: userId :: Nil =>
        new InputPage(userId.toLong).app

      case _ =>
        TimeLinePage.app


    }
  }

  def show(): Cancelable = {
    switchPageByHash()
    val page = currentPage
    mount(dom.document.body, page)
  }

  private var timer = 0

  def initSchedule(times: List[(Long, Long, Long)]):Unit = {
    dom.window.clearTimeout(timer)
    val curTask = times.head
    timer = dom.window.setTimeout(() => schedule(times, 0), curTask._2 - System.currentTimeMillis())
  }

  def schedule(times: List[(Long, Long, Long)], page:Byte):Unit = { //taskId,startTime,endTime  0->start 1->finish
    if(times.nonEmpty){
      println("today's tasks not finish")
      val curTask = times.head
      val nextTask = times(1)
      if(page == 0){
        goToPage("start", curTask._1)
        timer = dom.window.setTimeout(() => schedule(times, 1), curTask._3 - System.currentTimeMillis())
      }else{
        goToPage("finish", curTask._1)
        timer = dom.window.setTimeout(() => schedule(times.tail, 0), nextTask._2 - System.currentTimeMillis())
      }
    }
  }

  def goToPage(page: String, taskId: Long):Unit = {
    dom.window.location.hash = s"#/$page/$taskId"
  }


  def main(args: Array[String]): Unit = {
    Constant.fakeUserId = 10001
    TimeLinePage.getTodayTasks()
    show()
  }

}
