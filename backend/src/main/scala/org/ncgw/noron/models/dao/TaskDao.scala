package org.ncgw.noron.models.dao

import org.ncgw.noron.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object TaskDao {

  private val log = LoggerFactory.getLogger(this.getClass)
  private val tTask = SlickTables.tTask
  private val rTask = SlickTables.rTask

  def addTask(userId: Long, startTime: Long, content: String, taskType: Int, img: String) = db.run{
    if(img == ""){
      tTask += rTask(0l, userId, Some(content), None, Some(startTime), Some(0l), taskType, 0,0)
    }else{
      tTask += rTask(0l, userId, Some(content), Some(img), Some(startTime), Some(0l), taskType, 0,0)
    }
  }
}
