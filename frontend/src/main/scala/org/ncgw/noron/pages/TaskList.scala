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

  val taskList = Var(List[TaskItem]())

  def getTaskListByUser(userId: Int):Unit = {
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

  def deleteTask(taskId: Int) = {
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

  def createTaskItem(item: TaskItem):Elem = {
    <div class="tl-taskitem">
      <img class="tl-delete" src="" onclick={() =>
        deleteTask(item.taskId)
      }></img>
      <div class="tl-tasktime">
        <p>{item.startTime}</p>
        <p>TO</p>
        <p>{item.endTime}</p>
      </div>
      <div class="tl-taskcontent">
        {item.content}
      </div>
      <div class="tl-taskpro">
        <p>{Constant.taskTypeMap(item.taskType)}</p>
        <p>{Constant.priorityMap(item.priority)}</p>
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
          lst.map{l => createTaskItem(l)}
        }
      }}
    </div>

  def app:Node = {
    getTaskListByUser(Constant.fakeUserId)
    taskContent
  }
}
