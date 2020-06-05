package org.ncgw.noron.utils

//import org.json.JSONObject
import org.slf4j.LoggerFactory

import com.alibaba.fastjson.{JSON, JSONArray, JSONObject}

/**
  * Created by wyn on 2020/5/30.
  */
object NLPUtil {
//  private val log = LoggerFactory.getLogger(this.getClass)
  def text2keywords(text:String)= {
//    println(text)
    val javaTtK = new TextToKeywordUtil()
    val keywords = javaTtK.text2keywords(text).getJSONArray("items")
    println(keywords.length())
    var times=List("")
    var k=0
    for (i <- 0 until keywords.length()) {
      val keyword = keywords.getJSONObject(i)
      if (keyword.getString("pos")+keyword.getString("ne")=="TIME"){
        times=times ::: List(keyword.getString("item"))
      }
      println(keyword.getString("pos")+keyword.getString("ne")+keyword.getString("item")+keyword.get("basic_words").toString())
    }
    println(times.toString())
  }
  def main(args: Array[String]): Unit = {
    val text="6月5日上午9点半之前开3小时会"
    text2keywords(text)
  }

}