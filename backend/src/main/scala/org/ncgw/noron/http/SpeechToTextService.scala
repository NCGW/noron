package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import io.circe.Error
import org.ncgw.noron.shared.SpeechToTextProtocol.{GetParseTextReq, GetParseTextRsp}
import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
import org.ncgw.noron.utils.{NLPUtil, SpeechToTextUtil, TextToKeywordUtil}
import org.slf4j.LoggerFactory

/**
  * User: gaohan
  * Date: 2020/5/27
  * Time: 22:49
  */
trait SpeechToTextService extends ServiceUtils with SessionBase{

  import io.circe._
  import io.circe.generic.auto._

  private val log = LoggerFactory.getLogger(getClass)

  val speechToText = new SpeechToTextUtil



  //  private val getText =(path("getText") & post & pathEndOrSingleSlash ) {
//
//
//  }

  private val getParseText =(path("getParseText") & post & pathEndOrSingleSlash ) {
    entity(as[Either[Error, GetParseTextReq]]) {
      case Right(req) =>
        val text = speechToText.run(req.url)

        val parseText = NLPUtil.text2keywords(text)
        log.info("parseText=="+parseText)
        val content = parseText._1.toString().drop(6).dropRight(2).replaceAll(",", "").replace("。", "").replace("大概","").replace("大约","").replace("预计", "")

        val time  = parseText._2.get
        var startTime = 0l
        var endTime = 0l
        var ddl = 0l
        var during = 0l


        if(req.taskType == 1){
          val r = NLPUtil.parseDate4Agenda(time)
           startTime = r._1
           endTime = r._2

        } else if( req.taskType == 2){
          val r = NLPUtil.parseDate4Ass(time)
          ddl = r._1
          during = r._2
        }

      complete(GetParseTextRsp(req.taskType, content, Some(startTime), Some(endTime), Some(ddl), Some(during)))

      case Left(error) =>
        log.warn(s"parse error: $error")
        complete(ErrorRsp(100101,"parse error"))
    }

  }


  val parseText: Route =
    pathPrefix("content") {
      getParseText
    }


}
