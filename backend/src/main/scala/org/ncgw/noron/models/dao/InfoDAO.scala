package org.ncgw.noron.models.dao

import org.ncgw.norn.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: WYN
  * Date: 2020/6/19
  * Time: 16:27
  */
object InfoDAO {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val tTask = SlickTables.tTask

  def getInfoByTaskid(taskid:Long)={
    try{
      db.run(tTask.filter(_.taskId === taskid).result).map{info=>
        info.map { r =>
          val taskType=r.taskType
          val taskID=r.taskId
          val taskContent=r.taskContent match{
            case Some(s)=>s
            case None => "?"
          }
          val startTime=r.startTime match{
            case Some(s)=>s
            case None => (0).toLong
          }
          val endTime= r.endTime match{
            case Some(s)=>s
            case None => (0).toLong
          }
          val duringTime=endTime-startTime
          InfoClass(taskType,taskID,taskContent,startTime,duringTime,endTime)
        }.toList
      }
    } catch {
      case e: Throwable=>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }

  def delay(taskid:Long,delaymins:Long)=
    try{
      val new_startTime:Long=System.currentTimeMillis()+delaymins
      db.run(
        tTask.filter(_.taskId === taskid).map(_.startTime).update(Some(new_startTime))
      )
      Future.successful(1)
    }catch {
      case e: Throwable=>
        log.error(s"delay task:$taskid error: $e")
        Future.successful(-1)
    }

  def startorCancle(taskid:Long,type_sorc:String)=
    try{
      type_sorc match{
        case "start"=>
          db.run(
            tTask.filter(_.taskId === taskid).map(_.taskProgress).update(-1)
          )
        case "cancle"=>
          db.run(
            tTask.filter(_.taskId === taskid).map(_.taskProgress).update(-100)
          )
      }
      Future.successful(1)
    }catch {
      case e: Throwable=>
        log.error(s"start or cancle task:$taskid error: $e")
        Future.successful(-1)
    }
}
