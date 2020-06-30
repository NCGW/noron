package org.ncgw.noron.models.dao

import org.ncgw.noron.models.SlickTables._
import org.ncgw.noron.shared.TaskListProtocol.TaskItem
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory



object TaskDao {

  private val log = LoggerFactory.getLogger(this.getClass)

  def addTask(userId: Long, startTime: Long, content: String, taskType: Int, img: String) = db.run{
    if(img == ""){
      tTask += rTask(0l, userId, Some(content), None, Some(startTime), Some(0l), taskType, 0,0)
    }else{
      tTask += rTask(0l, userId, Some(content), Some(img), Some(startTime), Some(0l), taskType, 0,0)
    }
  }

  def getTaskList(userId: Int) = {
    val q = tTask.filter(_.userId === userId.toLong).map(i =>
      (i.taskId, i.startTime, i.endTime, i.taskContent, i.taskImg, i.taskType, i.taskProgress, i.priority)).result
    db.run(q)
  }
}
