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


}
