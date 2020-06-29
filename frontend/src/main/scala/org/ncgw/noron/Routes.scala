package org.ncgw.noron

/**
  * Created by haoshuhan on 2018/6/4.
  */
object Routes {

  private val base = "/noron"

  object ContentRoutes {

    private val baseUrl = base + "/content"
    val inputContent = baseUrl + "/inputContent"
    val startContent = baseUrl + "/startContent"
    val finishContent = baseUrl + "/finishContent"

  }

  object Upload{
    private val baseUrl = base + "/upload"

    val uploadPic = baseUrl + "/uploadPic"
  }

  object TimeLine{
    private val baseUrl = base + "/timeLine"

    val getTodaytasks = baseUrl + "/"
  }

  object TaskList{
    private val baseUrl = base + "/taskList"

    def getTaskList(uId: Int):String = baseUrl + s"?userId=$uId"
    def deleteTask(tId: Int):String = baseUrl + s"/delete?taskId=$tId"
  }

}
