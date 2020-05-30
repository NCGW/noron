package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import scala.language.postfixOps
import org.ncgw.noron.utils.Http
import org.ncgw.noron.Routes
import org.ncgw.norn.shared.CommonProtocol.{ContentInputRsp, ContentInputReq}
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom

object InputPage {

  def inputContent() : Unit = {

    val data = ContentInputReq(0l, "").asJson.noSpaces

    Http.postJsonAndParse[ContentInputRsp](Routes.ContentRoutes.inputContent, data).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {

        } else {

        }

      case Left(e) =>
        println(s"get roomList with ${e}")

    }
  }

  def app: xml.Node = {
    println("123")
    <div>
      <div style="text-align: center;">
        <input id="shuru" class="input-a" ></input>
      </div>
      <div style="display: flex; margin-top: 20%;">
        <button class="voice-button"><img src="/noron/static/img/语音输入.png" style="width:200px;"></img></button>
        <button class="voice-button"><img src="/noron/static/img/图片.png" style="width:188px;"></img></button>
      </div>
    </div>
  }
}
