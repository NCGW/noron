package org.ncgw.noron.utils

import java.text.SimpleDateFormat

import akka.http.scaladsl.server.Directives.complete
import org.ncgw.noron.models.dao.PRIDAO
import org.ncgw.noron.models.dao.PRIDAO.ScheduleClass
import org.ncgw.noron.shared.TimeLineProtocol.TimeLineItem

import scala.concurrent.ExecutionContext.Implicits.global
/**
  * User: WYN
  * Date: 2020/6/30
  * Time: 22:39
  */
object PRIUtil {
  case class TimeforWork(priority:Int,duringTime:Long,startTime:Long,endTime:Long)
  val format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm")   //设置要读取的时间字符串格式
//  println("2020-7-2 3:00 ",format1.parse("2020-7-2 3:00").getTime())
//  println("2020-7-2 8:00 ",format1.parse("2020-7-2 8:00").getTime())
//  println("2020-7-2 9:00 ",format1.parse("2020-7-2 9:00").getTime())
//  println("2020-7-2 9:30 ",format1.parse("2020-7-2 9:30").getTime())


  val format = new SimpleDateFormat("HH:mm")   //设置要读取的时间字符串格式
//  println("3:00 ",format.parse("3:00").getTime())
//  println("7:00 ",format.parse("7:00").getTime())
//  println("8:00 ",format.parse("8:00").getTime())
//  println("9:00 ",format.parse("9:00").getTime())
//  println("9:30 ",format.parse("9:30").getTime())

  val TimeList:List[(TimeforWork)]=List(
    TimeforWork(0,4*60*60*1000,format.parse("3:00").getTime(),format.parse("7:00").getTime()),
    TimeforWork(3,2*60*60*1000,format.parse("7:00").getTime(),format.parse("9:00").getTime()),
    TimeforWork(5,(2.5*60*60*1000).toLong,format.parse("9:00").getTime(),format.parse("11:30").getTime()),
    TimeforWork(4,1*60*60*1000,format.parse("11:30").getTime(),format.parse("12:30").getTime()),
    TimeforWork(2,1*60*60*1000,format.parse("12:30").getTime(),format.parse("13:30").getTime()),
    TimeforWork(4,(0.5*60*60*1000).toLong,format.parse("13:30").getTime(),format.parse("14:00").getTime()),
    TimeforWork(5,3*60*60*1000,format.parse("14:00").getTime(),format.parse("17:00").getTime()),
    TimeforWork(4,1*60*60*1000,format.parse("17:00").getTime(),format.parse("18:00").getTime()),
    TimeforWork(2,1*60*60*1000,format.parse("18:00").getTime(),format.parse("19:00").getTime()),
    TimeforWork(3,4*60*60*1000,format.parse("20:00").getTime(),format.parse("24:00").getTime()),
    TimeforWork(1,3*60*60*1000,format.parse("24:00").getTime(),format.parse("3:00").getTime()),
  )
  def priority={
    var spaceTime=TimeList
    var schedule=List(ScheduleClass(-1,-1,4*60*60*1000,format.parse("3:00").getTime(),format.parse("7:00").getTime(),0,"",None))
    val agandaT = PRIDAO.getAgendaPRI((10002).toLong).map{agandas=>
       agandas.map{aganda=>
         schedule :+= aganda    //把日程加入时间表
//         println(aganda,schedule)
         var flag=0;
         spaceTime.map{time=>
           val index=spaceTime.indexOf(time)
           if((aganda.startTime >= time.startTime && aganda.startTime<time.endTime) || flag==1){
             if(aganda.endTime <= time.endTime){      //case1:在整块时间内，更新spaceTime
//               println(1,aganda.taskid,aganda.startTime,time.startTime,time.endTime)
               val dt1=aganda.startTime-time.startTime
               val dt2=time.endTime-aganda.endTime
               val newspaceT1=TimeforWork(time.priority,dt1,time.startTime,aganda.startTime)
               val newspaceT2=TimeforWork(time.priority,dt2,aganda.endTime,time.endTime)
               if (dt1!=0 && dt2!=0){
//                 println(1,spaceTime(index),newspaceT1,newspaceT1)
                 spaceTime=spaceTime.updated(index,newspaceT1)
                 spaceTime :+= newspaceT2
               }
               if (dt1!=0 && dt2==0){
//                 println(2,spaceTime(index),newspaceT1)
                 spaceTime=spaceTime.updated(index,newspaceT1)
               }
               if (dt1==0 && dt2!=0){
//                 println(3,spaceTime(index),newspaceT2)
                 spaceTime=spaceTime.updated(index,newspaceT2)
               }
               if (dt1==0 && dt2==0){
                 val newspaceT=TimeforWork(time.priority,dt1,-30000000,aganda.startTime)
                 spaceTime=spaceTime.updated(index,newspaceT)
                 spaceTime.sortBy(_.startTime)
                 spaceTime.drop(1)
               }
               spaceTime.sortBy(_.startTime)
//               println(spaceTime)
             }
             else{          //case2:在不同时间内，更新spaceTime
//               println(2,aganda.taskid,aganda.startTime,aganda.endTime,time.startTime,time.endTime)
               if(aganda.startTime<=time.startTime && aganda.endTime>=time.endTime){
                 val newspaceT=TimeforWork(time.priority,0,-30000000,aganda.startTime)
                 spaceTime=spaceTime.updated(index,newspaceT)
                 spaceTime.sortBy(_.startTime)
                 spaceTime.drop(1)
               }
               else if(aganda.startTime<=time.startTime && aganda.endTime<time.endTime){
                 val dt=time.endTime-aganda.endTime
                 val newspaceT=TimeforWork(time.priority,dt,aganda.endTime,time.endTime)
                 if (dt!=0){
                   spaceTime=spaceTime.updated(index,newspaceT)
                   spaceTime.sortBy(_.startTime)
                 }
                 else{
                   val newspaceT=TimeforWork(time.priority,0,-30000000,aganda.startTime)
                   spaceTime=spaceTime.updated(index,newspaceT)
                   spaceTime.sortBy(_.startTime)
                   spaceTime.drop(1)
                 }
               }
               else if(aganda.endTime > time.endTime){
                 flag=1
                 val dt1=aganda.startTime-time.startTime
                 val newspaceT1=TimeforWork(time.priority,dt1,time.startTime,aganda.startTime)
                 if (dt1!=0){
                   spaceTime=spaceTime.updated(index,newspaceT1)
                 }
                 else{
                   val newspaceT=TimeforWork(time.priority,0,-30000000,aganda.startTime)
                   spaceTime=spaceTime.updated(index,newspaceT)
                   spaceTime.sortBy(_.startTime)
                   spaceTime.drop(1)
                 }
               }
             }
           }
         }
       }
    }
    val workT=PRIDAO.getWorkPRI((10001).toLong).map{works=>
        works.map{work=>
          println(work)
          val TN=work.UNprocess*work.duringTime
          spaceTime.map{time=>

          }
        }
    }
    Thread.sleep(1000*5)
//    println("1111111111",schedule)
//    println("2222222222",spaceTime)
//    print(agandaT)
    val fakeData=schedule.filter(_.taskid != -1).map{sch=>
      TimeLineItem(sch.taskid.toInt,sch.startTime,sch.endTime,sch.content,sch.Image)
    }
    Thread.sleep(1000*1)
    println(fakeData)
    fakeData
  }
}
