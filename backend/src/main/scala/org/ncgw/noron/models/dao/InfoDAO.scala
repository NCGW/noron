package org.ncgw.noron.models.dao

import org.ncgw.noron.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import org.ncgw.noron.utils.PRIUtil.schedule

/**
  * User: WYN
  * Date: 2020/6/19
  * Time: 16:27
  */
object InfoDAO {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val tTask = SlickTables.tTask

  def getInfoByTaskidFromPRI(taskid:Long)={
    schedule.filter(_.taskid == taskid).map {sch=>
      InfoClass(sch.tasktype, sch.taskid, sch.content, sch.startTime+System.currentTimeMillis()/86400000*86400000, sch.duringTime, sch.endTime+System.currentTimeMillis()/86400000*86400000)
    }
  }

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
          taskType match{
            case 0=>
              val duringTime=endTime-startTime
              InfoClass(taskType,taskID,taskContent,startTime,duringTime,endTime)
            case 1=>
              val duringTime=startTime
              InfoClass(taskType,taskID,taskContent,endTime-duringTime,duringTime,endTime)
            case 2=>
              val duringTime=startTime
              InfoClass(taskType,taskID,taskContent,System.currentTimeMillis(),duringTime,endTime)
          }
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
      val new_startTime:Long=System.currentTimeMillis()+delaymins*60*1000
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
            tTask.filter(_.taskId === taskid).map(_.startTime).update(Some(System.currentTimeMillis()))
          )
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


  def updateProgress(taskId: Long, progress: Int)= db.run{
    tTask.filter(_.taskId === taskId).map(_.taskProgress).update(progress)
  }


}
