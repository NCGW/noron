package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._

/**
  * User: gaohan
  * Date: 2020/5/27
  * Time: 22:49
  */
trait SpeechToTextService extends ServiceUtils with SessionBase{

   val speechToText =(pathPrefix("statistics") & get){
    pathEndOrSingleSlash{
      getFromResource("html/statistics.html")
    }
  }


}
