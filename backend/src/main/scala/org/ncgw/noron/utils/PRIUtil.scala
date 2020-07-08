package org.ncgw.noron.utils

import java.text.SimpleDateFormat

import akka.http.scaladsl.server.Directives.complete
import org.ncgw.noron.models.dao.PRIDAO
import org.ncgw.noron.models.dao.PRIDAO.ScheduleClass
import org.ncgw.noron.shared.TimeLineProtocol.TimeLineItem

import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.control.Breaks._
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

  var schedule_today=List(TimeLineItem(-1, 0, 0, "", None))
  var schedule=List(ScheduleClass(-1,-1,-1,4*60*60*1000,format.parse("3:00").getTime(),format.parse("7:00").getTime(),0,"",None))

  def priority={
    val userID=10001
    var spaceTime=TimeList
    val agandaT = PRIDAO.getAgendaPRI(userID.toLong)
    Thread.sleep(2000)
    agandaT.foreach{agandas=>
       agandas.foreach{aganda=>
         println("AGENDA:",aganda)
         schedule :+= aganda    //把日程加入时间表
//         println(aganda,schedule)
         var flag=0
         spaceTime.foreach{time=>
           val index=spaceTime.indexOf(time)
           if((aganda.startTime >= time.startTime && aganda.startTime<=time.endTime) || flag==1){
             if(aganda.endTime <= time.endTime){      //case1:在整块时间内，更新spaceTime
               println("case11:")
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
                 val newspaceT=TimeforWork(-1,0,-300000000,-300000000)
                 spaceTime=spaceTime.updated(index,newspaceT)
                 spaceTime.sortBy(_.startTime)
                 spaceTime.drop(1)
               }
               spaceTime.sortBy(_.startTime)
//               println(spaceTime)
             }
             else{          //case2:在不同时间内，更新spaceTime
               println("case22:")
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
    Thread.sleep(1000)
    var spacetime_st=spaceTime
    var spacetime_p=spaceTime.sortBy(_.priority).reverse
//    println("spacetime_p:",spacetime_p)
//    println("spaceTime:",spaceTime)
//    println("spacetime_st:",spacetime_st)
    val workT=PRIDAO.getWorkPRI(userID.toLong)
    Thread.sleep(2000)
    println(agandaT)
    println(workT)
    workT.foreach { works =>
      println(works)
      for(work <- works) {
        println("WORK:",work)
        val TN = work.UNprocess * work.duringTime / 100
        val DDL = work.endTime
        val P = work.priority
        var flag=1
        if (P > 50) {
          for (time <- spacetime_p){
//            println("TIME:",time)
//            println("spaceTime:",spaceTime)
            val timeinTime=spaceTime find( _.startTime == time.startTime) match{
              case Some(s)=>s
              case other=>TimeforWork(-1,0,0,0)
            }
            val index=spaceTime.indexOf(timeinTime)
//            println("Index:",index)
            if (time.priority >= 4  && flag==1) {
              if (time.duringTime >= TN) { //case1:空闲时间足够安排，从头开始，直接安排
                println("case1")
//                println("spacetime_p1",spacetime_p)
                //加入日程表
                val st_sche = time.startTime
                val dt_sche = TN
                val et_sche = st_sche + dt_sche
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成100%", work.Image)
                //更新空闲时间
                val st_space = et_sche
                val et_space = time.endTime
                val dt_space = et_space - st_space
                if (dt_space != 0) { //更新
//                  print(dt_sche,st_sche,et_sche)
//                  println(dt_space,st_space,et_space)
                  val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                else { //删除
                  val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spaceTime.drop(1)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
//                println("spacetime_p2:",spacetime_p)
                flag=0
              }
              else if (time.duringTime >= (TN * 0.4)  && flag==1) { //case2:空闲时间够安排40%,先安排40%,更新work
                println("case2")
                //加入日程表
                val st_sche = time.startTime
                val dt_sche = (TN * 0.4).toLong
                val et_sche = st_sche + dt_sche
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成40%", work.Image)
                //更新空闲时间
                val st_space = time.startTime + et_sche
                val et_space = time.endTime
                val dt_space = et_space - st_space

                if (dt_space != 0) { //更新
//                  print(dt_sche,st_sche,et_sche)
//                  println(dt_space,st_space,et_space)
                  val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                else { //删除
                  val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spaceTime.drop(1)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                flag=0
              }
              else { //空闲时间能够安排不够40%
                val ndtb = time.duringTime + spacetime_st(index + 1).duringTime //与后一时间段连接起来
                val ndtf = time.duringTime + spacetime_st(index - 1).duringTime //与前一时间段连接起来
                val ndt = time.duringTime + spacetime_st(index - 1).duringTime + spacetime_st(index + 1).duringTime //与前后时间段连接起来
                if (ndtb >= (0.4 * TN) && time.endTime == spacetime_st(index + 1).startTime  && flag==1) { //case3:与后一时间段连接起来达到40%，安排
                  println("case3")
                  //加入日程表
                  val st_sche = time.startTime
                  val dt_sche = (TN * 0.4).toLong
                  val et_sche = st_sche + dt_sche
                  schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成40%", work.Image)
                  //更新空闲时间
                  //更新前一段为空
                  val newspaceT1 = TimeforWork(-1, 0, -300000000, time.startTime)
                  spaceTime = spaceTime.updated(index, newspaceT1)
                  //更新后一段
                  val st_space = spacetime_st(index + 1).startTime + dt_sche - spacetime_st(index).duringTime
                  val et_space = spacetime_st(index + 1).endTime
                  val dt_space = et_space - st_space

                  if (dt_space != 0) { //更新
                    val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                    spaceTime = spaceTime.updated(index + 1, newspaceT)
                    //删除空的前一段
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(1)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse

                  }
                  else { //删除
                    val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                    spaceTime = spaceTime.updated(index + 1, newspaceT)
                    //删除全部
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(2)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse
                  }
                  flag=0
                }
                else if (ndtf >= (0.4 * TN) && time.startTime == spacetime_st(index - 1).endTime  && flag==1) { //case4:与前一时间段连接起来达到40%，安排40%
                  println("case4")
                  //加入日程表
                  val dt_sche = (TN * 0.4).toLong
                  //                    val st_sche=spacetime_st(index-1).endTime-(dt_sche-time.duringTime)
                  val st_sche = time.endTime - dt_sche
                  val et_sche = time.endTime
                  schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成40%", work.Image)
                  //更新空闲时间
                  //更新后一段为空
                  val newspaceT1 = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT1)
                  //更新前一段
                  val st_space = spacetime_st(index - 1).startTime
                  val et_space = st_sche
                  val dt_space = et_space - st_space
                  if (dt_space != 0) {
                    val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                    spaceTime = spaceTime.updated(index - 1, newspaceT)
                    //删除空的后一段
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(1)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse
                  }
                  else {
                    val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                    spaceTime = spaceTime.updated(index - 1, newspaceT)
                    //删除全部
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(2)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse
                  }
                  flag=0
                }
                else if (ndt >= (0.4 * TN) && time.startTime == spacetime_st(index - 1).endTime && time.endTime == spacetime_st(index + 1).startTime  && flag==1) {
                  //case5:与前后时间段连接起来达到40%，安排
                  println("case5")
                  //加入日程表
                  val dt_sche = (TN * 0.4).toLong
                  val et_sche = spacetime_st(index + 1).endTime
                  //val st_sche=spacetime_st(index-1).endTime-(dt_sche-time.duringTime)
                  val st_sche = et_sche - dt_sche
                  schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成40%", work.Image)
                  //更新空闲时间
                  //更新中间一段为空
                  val newspaceT1 = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT1)
                  //更新后一段为空
                  spaceTime = spaceTime.updated(index + 1, newspaceT1)
                  //更新前一段
                  val st_space = spacetime_st(index - 1).startTime
                  val et_space = st_sche
                  val dt_space = et_space - st_space
                  val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                  if (dt_space != 0) {
                    val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                    spaceTime = spaceTime.updated(index - 1, newspaceT)
                    //删除空的后两段
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(2)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse
                  }
                  else {
                    val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                    spaceTime = spaceTime.updated(index - 1, newspaceT)
                    //删除全部
                    spaceTime.sortBy(_.startTime)
                    spaceTime.drop(3)
                    //更新
                    spacetime_st = spaceTime
                    spacetime_p = spaceTime.sortBy(_.priority).reverse
                  }
                  flag=0
                }
                else if(flag==1) { //case6:合并前后时间段仍不能完成40%以上，则将核心工作时间排满，完成多少算多少
                  println("case6")
                  //加入日程表
                  val dt_sche = time.duringTime
                  val st_sche = time.startTime
                  val et_sche = time.endTime
                  val expP = (100 * dt_sche / TN ).toString
                  schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成" + expP + "%", work.Image)
                  //更新空闲时间,直接删除该时间段
                  val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spaceTime.drop(1)
                  //更新
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                  flag=0
                }
              }
            }
            else if (time.priority >= 2  && flag==1) {
              val index = spacetime_st.indexOf(time)
              if (time.duringTime >= TN) { //case7:空闲时间足够安排，直接安排
                println("case7")
                //加入日程表
                val st_sche = time.startTime
                val dt_sche = TN
                val et_sche = st_sche + dt_sche
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成100%", work.Image)
                //更新空闲时间
                val st_space = time.startTime + et_sche
                val et_space = time.endTime
                val dt_space = et_space - st_space
                val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                if (dt_space != 0) { //更新
                  val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                else { //删除
                  val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spaceTime.drop(1)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                flag=0
              }
              else if( flag==1){ //case8:空闲时间不够，能排多少排多少
                println("case8")
                val dt_sche = time.duringTime
                val st_sche = time.startTime
                val et_sche = time.endTime
                val expP = (100 * dt_sche / TN ).toString
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成" + expP + "%", work.Image)
                //更新空闲时间,直接删除该时间段
                val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                spaceTime = spaceTime.updated(index, newspaceT)
                spaceTime.sortBy(_.startTime)
                spaceTime.drop(1)
                //更新
                spacetime_st = spaceTime
                spacetime_p = spaceTime.sortBy(_.priority).reverse
                flag=0
              }
            }
          }
        }
        else { //紧急任务都排完了，核心时间有空余，安排低级别任务
          spacetime_p.foreach { time =>
            val index = spaceTime.indexOf(time)
            if (time.priority >= 5 && time.duringTime >= 10 * 60 * 1000 && flag==1) {
              if (time.duringTime >= TN ) { //case9:空闲时间足够安排，直接安排
                println("case9")
                //加入日程表
                val st_sche = time.startTime
                val dt_sche = TN
                val et_sche = st_sche + dt_sche
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成100%", work.Image)
                //更新空闲时间
                val st_space = time.startTime + et_sche
                val et_space = time.endTime
                val dt_space = et_space - st_space
                val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                if (dt_space != 0) { //更新
                  val newspaceT = TimeforWork(time.priority, dt_space, st_space, et_space)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                else { //删除
                  val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                  spaceTime = spaceTime.updated(index, newspaceT)
                  spaceTime.sortBy(_.startTime)
                  spaceTime.drop(1)
                  spacetime_st = spaceTime
                  spacetime_p = spaceTime.sortBy(_.priority).reverse
                }
                flag=0
              }
              else if(flag==1){ //case10:空闲时间不够，能排多少排多少
                println("case10")
                val dt_sche = time.duringTime
                val st_sche = time.startTime
                val et_sche = time.endTime
                val expP = (100 * dt_sche / TN ).toString
                schedule :+= ScheduleClass(work.tasktype,work.taskid, work.priority, dt_sche, st_sche, et_sche, work.UNprocess, work.content + "-完成" + expP + "%", work.Image)
                //更新空闲时间,直接删除该时间段
                val newspaceT = TimeforWork(-1, 0, -300000000, -300000000)
                spaceTime = spaceTime.updated(index, newspaceT)
                spaceTime.sortBy(_.startTime)
                spaceTime.drop(1)
                //更新
                spacetime_st = spaceTime
                spacetime_p = spaceTime.sortBy(_.priority).reverse
                flag=0
              }
            }
          }
        }
      }
    }
    Thread.sleep(1000*10)
//    println("1111111111",schedule)
//    println("2222222222",spaceTime)
//    print(agandaT)

    schedule_today=schedule.filter(_.taskid != -1).sortBy(_.startTime).map{sch=>
      println("SCHEDULE:",sch)
      TimeLineItem(sch.taskid.toInt,sch.startTime,sch.endTime,sch.content,sch.Image)
    }
    println("schedule_today:",schedule_today)
  }
}
