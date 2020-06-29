package org.ncgw.noron

import org.ncgw.noron.Routes.ContentRoutes.baseUrl

/**
  * Created by haoshuhan on 2018/6/4.
  */
object Routes {

  private val base = "/noron"

  object ContentRoutes {

    private val baseUrl = base + "/content"
    val inputContent = baseUrl + "/inputContent"

    val getParseText = baseUrl + "/getParseText"

  }

  object Upload{
    private val baseUrl = base + "/upload"

    val uploadPic = baseUrl + "/uploadPic"
  }

  object Finish{
    private val baseUrl = base + "/finishContent"

    val getInfo = baseUrl + "/getInfo"

    val updateProgress = baseUrl + "/updateProgress"

    val updateeReward = baseUrl + "/updateReward"

  }
  object Start {
    private val baseUrl = base + "/startContent"
    val getInfo = baseUrl + "/getInfo"
    val delay = baseUrl + "/delay"
    val startorcancel = baseUrl + "/startorcancel"
  }

  object Rank{
    private val baseUrl = base + "/rank"

    val getRank = baseUrl + "/getRank"

    val getUserRank = baseUrl + "/getUserRank"
  }


}
