package org.ncgw.noron.utils

//import org.json.JSONObject
import org.slf4j.LoggerFactory

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

/**
  * Created by wyn on 2020/5/30.
  */
object NLPUtil {
//  private val log = LoggerFactory.getLogger(this.getClass)
  def text2keywords(text:String):(List[String], Option[List[String]])= {
//    println(text,taskType)
    val javaTtK = new TextToKeywordUtil()
    val keywords = javaTtK.text2keywords(text).getJSONArray("items")
    //println(keywords.length())
    var times=List.empty[String]
    var taskContent = List.empty[String]
    var k=0
    for (i <- 0 until keywords.length()) {
      val keyword = keywords.getJSONObject(i)
      if (keyword.getString("pos")+keyword.getString("ne")=="TIME"){
        times = times ::: List(keyword.getString("item"))
      } else {
        taskContent = taskContent ::: List(keyword.getString("item"))
      }
      //println(keyword.getString("pos")+keyword.getString("ne")+keyword.getString("item")+keyword.get("basic_words").toString())
    }
//    println(times.toString())
//    println("======"+ taskContent.toString())
    (taskContent, Some(times))
  }

  val transferMap =  Map(
    "一" -> 1, "二" -> 2, "三" -> 3, "四" -> 4, "五" -> 5, "六" -> 6, "七" -> 7, "八" -> 8, "九" -> 9, "十" -> 10, "十一" -> 11, "十二" -> 12,
    "日" -> 7, "十三" -> 13, "十四" -> 14, "十五" -> 15, "十六" -> 16, "十七" -> 17, "十八" -> 18, "十九" -> 19, "二十" -> 20, "二十一" -> 21, "二十二" -> 22,
    "二十三" -> 23, "二十四" -> 24, "二十五" -> 25, "二十六" -> 26, "二十七" -> 27, "二十八" -> 28, "二十九" -> 29, "三十" -> 30, "三十一" -> 31,"两" -> 2


  )


def parseHour(tt: String, flag: Boolean)= {

  var startTime = 0
  var endtime = 0

  if(tt.contains("上午")){
    val j = tt.indexOf("午")
    val i = tt.indexOf("点")

    if(transferMap.contains(tt.substring(j + 1, i))) {
      startTime = transferMap(tt.substring(j + 1, i))
    } else {
      startTime = tt.substring(j + 1, i).toInt
    }

    val ii = tt.indexOf("点",i+1)


    if(flag){
      if(transferMap.contains(tt.substring(i+2, ii ))) {
        endtime = transferMap(tt.substring(i+2, ii ))
      } else {
        endtime = tt.substring(i+2, ii).toInt
      }

    }


  } else if(tt.contains("下午") || tt.contains("晚上")) {
    val j = tt.indexOf("午")
    val i = tt.indexOf("点")

    if(transferMap.contains(tt.substring(j + 1, i))) {
      startTime = transferMap(tt.substring(j + 1, i)) + 12
    } else {
      startTime = tt.substring(j + 1, i).toInt + 12
    }

    val ii = tt.indexOf("点",i+1)

    if(flag){
      if(transferMap.contains(tt.substring(i+2, ii))) {
        endtime = transferMap(tt.substring(i+2, ii )) + 12
      } else {
        endtime = tt.substring(i+2, ii).toInt + 12
      }
    }


  }

  (startTime, endtime)

}

  def parseDate4Agenda(t1: List[String]) = {

    var start = 0l
    var end = 0l

    if(t1.head.contains("月")){
      var month = 0
      val m = t1.head.dropRight(1)

      if(transferMap.contains(m)) {
        month = transferMap(m)
      } else {
        month = m.toInt
      }

      val tt = t1.tail.head
      var day = 0
      var d = ""
      if(tt.contains("日")){
        d = tt.take(2).replace("日", "")

      } else if(tt.contains("号")){
         d = tt.take(2).replace("号", "")
      }

      if(transferMap.contains(d)) {
        day = transferMap(d)
      } else {
        day = d.toInt
      }
      val startTime = parseHour(tt, true)._1
      val endTime = parseHour(tt, true)._2

      val startDate = "2020-" + month + "-" + day + " " + startTime +":00:00"
      val endData = "2020-" + month + "-" + day + " " + endTime +":00:00"

      println("------"+ startDate)

      start = TimeUtil.date2TimeStamp(startDate).right.get
      end = TimeUtil.date2TimeStamp(endData).right.get



    } else if(t1.head.contains("周")){

      val month = TimeUtil.getMonth()
      var date = TimeUtil.getDateofMonth()
      val tt = t1.head

      val i = transferMap(t1.head.substring(1, 2))

      val bias = if(i - TimeUtil.getWeek()>= 0) i - TimeUtil.getWeek() else i - TimeUtil.getWeek()+ 7

      date = date + bias
      val startTime = parseHour(tt, true)._1
      val endTime = parseHour(tt, true)._2

      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"
      val endData = "2020-" + month + "-" + date + " " + endTime +":00:00"


      start = TimeUtil.date2TimeStamp(startDate).right.get
      end = TimeUtil.date2TimeStamp(endData).right.get


    } else if(t1.head.contains("明天")){
      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth() + 1
      val tt = t1.head
      val startTime = parseHour(tt, true)._1
      val endTime = parseHour(tt, true)._2


      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"
      val endData = "2020-" + month + "-" + date + " " + endTime +":00:00"


      start = TimeUtil.date2TimeStamp(startDate).right.get
      end = TimeUtil.date2TimeStamp(endData).right.get


    } else if(t1.head.contains("后天")){
      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth() + 2
      val tt = t1.head

      val startTime = parseHour(tt, true)._1
      val endTime = parseHour(tt, true)._2


      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"
      val endData = "2020-" + month + "-" + date + " " + endTime +":00:00"

      start = TimeUtil.date2TimeStamp(startDate).right.get
      end = TimeUtil.date2TimeStamp(endData).right.get

    } else{
      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth()
      val tt = t1.head

      val startTime = parseHour(tt, true)._1
      val endTime = parseHour(tt, true)._2


      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"
      val endData = "2020-" + month + "-" + date + " " + endTime +":00:00"


      start = TimeUtil.date2TimeStamp(startDate).right.get
      end = TimeUtil.date2TimeStamp(endData).right.get

    }

    (start,end)

  }

  def parseDate4Ass(t11: List[String]) = {
    var ddl = 0l
    var during = 0l

    val st = t11.head
    val dt = t11.tail.head


    if(st.contains("月")){
      val i = st.indexOf("月")
      var month = 0
      val m = st.substring(0, i)

      if(transferMap.contains(m)) {
        month = transferMap(m)
      } else {
        month = m.toInt
      }
      var j = 0
      if(st.contains("日")){
         j = st.indexOf("日")

      } else if(st.contains("号")){
        j = st.indexOf("号")

      }
      var day = 0
      val d = st.substring(i+ 1, j)

      if(transferMap.contains(d)) {
        day = transferMap(d)
      } else {
        day = d.toInt
      }

      val startTime = parseHour(st, false)._1

      val startDate = "2020-" + month + "-" + day + " " + startTime +":00:00"


      ddl = TimeUtil.date2TimeStamp(startDate).right.get

    } else if(st.contains("周")){

      val month = TimeUtil.getMonth()
      var date = TimeUtil.getDateofMonth()

      val i = transferMap(st.substring(1, 2))

      val bias = if(i - TimeUtil.getWeek()>= 0) i - TimeUtil.getWeek() else i - TimeUtil.getWeek()+ 7

      date = date + bias
      val startTime = parseHour(st, false)._1

      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"

      ddl = TimeUtil.date2TimeStamp(startDate).right.get


    } else if(st.contains("明天")){

      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth() + 1
      val startTime = parseHour(st, false)._1
      val endTime = parseHour(st, false)._2


      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"

      ddl = TimeUtil.date2TimeStamp(startDate).right.get


    } else if(st.contains("后天")){
      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth() + 2

      val startTime = parseHour(st, false)._1


      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"


      ddl = TimeUtil.date2TimeStamp(startDate).right.get

    } else{
      val month = TimeUtil.getMonth()
      val date = TimeUtil.getDateofMonth()

      val startTime = parseHour(st, false)._1

      val startDate = "2020-" + month + "-" + date + " " + startTime +":00:00"

      ddl = TimeUtil.date2TimeStamp(startDate).right.get

    }



    if(dt.contains("小时") && !dt.contains("分钟")){
      val i = dt.indexOf("小时")
      var hour = 0
      val h = dt.substring(0, i)


      if(transferMap.contains(h)) {
        hour = transferMap(h)
      } else {
        hour = h.toInt
      }

      during = hour * 60 * 60 * 1000


    } else if(dt.contains("分钟") && !dt.contains("小时")){

      val i = dt.indexOf("分钟")
      var minte = 0
      val m = dt.substring(0, i)


      if(transferMap.contains(m)) {
        minte = transferMap(m)
      } else {
        minte = m.toInt
      }

      during = minte * 60 * 1000

    }


    (ddl, during)


  }

  def main(args: Array[String]): Unit = {
    val text1="十月二十日上午十点至十一点开会"
    val text11 = "周一下午2点到4点上英语课"
    val text12 = "今天下午3点到6点做汇报"
    val text2="七月15日下午10点交作业，预计3小时"
    val text21 = "周一下午2点上英语课，大概2小时"
    val text22 = "后天下午3点做汇报， 大概10分钟"
    val text3="看电影《中国女排》"

    val t1 = text2keywords(text12)._2.get

   //val  a = parseDate4Ass(t1)

//
    val b = parseDate4Agenda(t1)

//
    println("a=="+ b._1)
    println("b=="+ b._2)
//

  }

}
