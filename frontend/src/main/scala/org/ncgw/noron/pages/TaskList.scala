package org.ncgw.noron.pages

import mhtml.{Var, emptyHTML}
import org.ncgw.noron.Routes
import org.ncgw.noron.utils.{Http, TimeTool}
import concurrent.ExecutionContext.Implicits.global
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.noron.shared.SuccessRsp
import org.ncgw.noron.shared.TaskListProtocol._
import org.ncgw.noron.common.Constant
import scala.scalajs.js.Date
import scala.xml.{Elem, Node}


/**
  * Author: wmy
  * Date: 2020/06/26
  * Desc: 任务列表管理
  */
object TaskList {

  val fakeData = List(TaskItem(0,1593425053417L, 1593425053417L, "read", None, 0, 60, 0),
    TaskItem(1,1593425053417L, 1593425053417L, "read", None, 1, 50, 1),
    TaskItem(2,1593425053417L, 1593425053417L, "read", None, 2, 0, 2))
  val taskList = Var(fakeData)

  def getTaskListByUser(userId: Long):Unit = {
    Http.getAndParse[TaskListRsp](Routes.TaskList.getTaskList(userId)).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          taskList := rsp.taskList
        }else{
          println("get task list error", rsp.msg)
        }

      case Left(e) =>
        println("parse json error")
    }
  }

  def deleteTask(taskId: Long):Unit = {
    Http.getAndParse[SuccessRsp](Routes.TaskList.deleteTask(taskId)).map{
      case Right(rsp) =>
        if(rsp.errCode == 0){
          println(s"delete task id-$taskId")
          taskList.update(l => l.filterNot(_.taskId == taskId))
        }else{
          println("delete task id error", rsp.msg)
        }

      case Left(e) =>
        println("parse json error")
    }
  }

  def calPriority(pri: Int) = {
    if(pri > 100) "高" else "低"
  }

  def createTaskItem(item: TaskItem):Elem = {
    <div class="tl-taskitem">
      <img class="tl-delete" src="/noron/static/img/close.png" onclick={() =>
        deleteTask(item.taskId)
      }></img>
      <div class="tl-tasktime">
        <p>{TimeTool.DateFormatter(new Date(item.startTime), "hh:mm")}</p>
        <p style="color:#ffc340;font-size:45px;line-height:50px;">TO</p>
        <p>{TimeTool.DateFormatter(new Date(item.startTime), "hh:mm")}</p>
      </div>
      <div class="tl-taskcontent">
        {item.content}
      </div>
      <div class="tl-taskpro">
        <p>{Constant.taskTypeMap(item.taskType)}</p>
        <p>{calPriority(item.priority)}</p>
        <p>{item.taskProgress}%</p>
      </div>
    </div>
  }

  val taskContent =
    <div>
      {taskList.map{lst =>
        if(lst.isEmpty){
          emptyHTML
        }else{
          <div>
            {lst.map{l => createTaskItem(l)}}
          </div>
        }
      }}
    </div>

  def app:Node = {
    getTaskListByUser(Constant.fakeUserId)
    taskContent
  }
}
