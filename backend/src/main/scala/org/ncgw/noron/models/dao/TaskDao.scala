package org.ncgw.noron.models.dao

import org.ncgw.noron.models.SlickTables._
import org.ncgw.noron.shared.TaskListProtocol.TaskItem
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db

object TaskDao {
  
  def getTaskList(userId: Int) = {
    val q = tTask.filter(_.userId === userId.toLong).map(i => 
      (i.taskId, i.startTime, i.endTime, i.taskContent, i.taskImg, i.taskType, i.taskProgress, i.priority)).result
    db.run(q)
  }

}
