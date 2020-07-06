package org.ncgw.noron.models.dao

import org.ncgw.noron.models.SlickTables
import org.ncgw.noron.models.dao.InfoDAO.{log, tTask}
import org.ncgw.noron.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory
import slick.jdbc.PostgresProfile.api._

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

/**
  * User: WYN
  * Date: 2020/6/30
  * Time: 22:43
  */
object PRIDAO {
  private val log = LoggerFactory.getLogger(this.getClass)
  private val tUser = SlickTables.tUser
  private val tTask = SlickTables.tTask
  case class ScheduleClass(taskid:Long,priority:Int,duringTime:Long,startTime:Long,endTime:Long,UNprocess:Int,content:String,Image:Option[String])
  def renewPRI={
    try{
      db.run(tTask.filter(_.taskType === 0).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).result).map { agendas =>
        agendas.map {agenda=>
          val ST = agenda.startTime match{
            case Some(s)=> s
            case None=> 0.toLong
          }
          val ET=agenda.endTime match{
            case Some(s)=> s
            case None=> 0.toLong
          }
          val TN=(100-agenda.taskProgress) * (ET-ST)
          val TC=ST-System.currentTimeMillis()
//          val TC=ST-"1593630000000".toLong
          val P=100*TN/(TC+1)
//          print(ET-ST,ST-System.currentTimeMillis()+1)
//          println(agenda.taskType,P)
          db.run(
            tTask.filter(_.taskId === agenda.taskId).map(_.priority).update(P.toInt)
          )
        }
      }
      db.run(tTask.filter(_.taskType === 1).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).result).map { works =>
        works.map {work=>
          val DT = work.startTime match{
            case Some(s)=> s
            case None=> 0.toLong
          }
          val TN=(100-work.taskProgress) * DT
          val DDL=work.endTime match{
            case Some(s)=> s
            case None=> 0.toLong
          }
          val TC=DDL-System.currentTimeMillis()
//          val TC=DDL-"1593630000000".toLong
//          print(TN,TC)
          val P=100*TN/(TC+1)
//          println(work.taskType,P)
          db.run(
            tTask.filter(_.taskId === work.taskId).map(_.priority).update(P.toInt)
          )
        }
      }
      db.run(tTask.filter(_.taskType === 2).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).result).map { wishes =>
        wishes.map {wish=>
          val P=1
//          println(wish.taskType,P)
          db.run(
            tTask.filter(_.taskId === wish.taskId).map(_.priority).update(P.toInt)
          )
        }
      }
      Future.successful(1)
    } catch {
      case e: Throwable=>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }
  def getAgendaPRI(userid:Long)= {
    try{
      db.run(tTask.filter(_.userId === userid).filter(_.taskType === 0).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).filter(_.startTime >= (System.currentTimeMillis()+86400000)).result).map { agendas =>
//      db.run(tTask.filter(_.userId === userid).filter(_.taskType === 0).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).filter(_.startTime <= ("1593630000000".toLong+86400000)).result).map { agendas =>
        agendas.map { agenda =>
          val TCon = agenda.taskContent match {
            case Some(s) => s
            case None => ""
          }
          val ST = agenda.startTime match {
            case Some(s) => s
            case None => 0.toLong
          }
          val ET = agenda.endTime match {
            case Some(s) => s
            case None => 0.toLong
          }
          val DT = ET - ST
//          println("DDDDDDDDDDDD",DT)
//          ScheduleClass(agenda.taskId, agenda.priority, DT, (ST-"1593630000000".toLong)-5*60*60*1000, (ET-"1593630000000".toLong)-5*60*60*1000,(100-agenda.taskProgress),TCon,agenda.taskImg)
          ScheduleClass(agenda.taskId, agenda.priority, DT, (ST-System.currentTimeMillis())-5*60*60*1000, (ET-System.currentTimeMillis())-5*60*60*1000,(100-agenda.taskProgress),TCon,agenda.taskImg)
        }.toList
      }
    } catch {
      case e: Throwable=>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }
  def getWorkPRI(userid:Long)= {
    try{
      //      db.run(tTask.filter(_.userId === userid).filter(_.taskType === 0).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).filter(_.startTime >= (System.currentTimeMillis()+86400000)).result).map { agendas =>
      db.run(tTask.filter(_.userId === userid).filter(_.taskType === 1).filter(_.taskProgress =!= 100).filter(_.taskProgress =!= -100).sortBy(_.priority).result).map { works =>
        works.map { work =>
          val TCon = work.taskContent match {
            case Some(s) => s
            case None => ""
          }
          val DT = work.startTime match {
            case Some(s) => s
            case None => 0.toLong
          }
          val ET = work.endTime match {
            case Some(s) => s
            case None => 0.toLong
          }
          //          println("DDDDDDDDDDDD",DT)
//          ScheduleClass(work.taskId, work.priority, DT, -3000000, (ET-"1593630000000".toLong)-5*60*60*1000,(100-work.taskProgress),TCon,work.taskImg)
          ScheduleClass(work.taskId, work.priority, DT, -3000000, (ET-System.currentTimeMillis())-5*60*60*1000,(100-work.taskProgress),TCon,work.taskImg)
        }.toList.reverse
      }
    } catch {
      case e: Throwable=>
        log.error(s"get recordList error with error $e")
        Future.successful(Nil)
    }
  }



}
