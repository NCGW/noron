package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.norn.shared.CommonProtocol.{ContentInputReq, ContentInputRsp, UploadPicReq}
import org.ncgw.norn.shared.SuccessRsp
import scala.language.postfixOps
import org.ncgw.noron.utils.Http
import org.ncgw.noron.Routes
import org.ncgw.noron.utils.JsFunc
import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw.FormData

object InputPage {

  val test = List(("上午九点","打卡"),("上午十点","晨会"),("下午三点","汇报"))
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

  def upload(input: Input): Unit = {
    val uploadName = input.value.split("\\\\").last
    val data = UploadPicReq(0l, uploadName).asJson.noSpaces
    Http.postJsonAndParse[SuccessRsp](Routes.Upload.uploadPic, data).map {

      case Right(r) =>
        if (r.errCode == 0) {
          JsFunc.alert("上传成功")
        }

      case Left(error) =>
        JsFunc.alert(s"上传失败，error：$error")
    }
  }

  def app: xml.Node = {
    println("123")
    <div>
      <div style="text-align: center;">
        <input id="shuru" class="input-a" ></input>
      </div>
      {
      test.map{ v =>
          <div class="plane">
            <div class="plane-time">{v._1}</div>
            <div class="plane-todo">{v._2}</div>
          </div>

      }
      }
      <div style="display: flex; margin-top: 20%;">
        <button class="voice-button"><img src="/noron/static/img/语音输入.png" style="width:100px;"></img></button>
        <div class="upload">
          <img src="/noron/static/img/图片.png" style="width:84px; height:84px"></img>
          <input type="file" class="pic-input" name="file" onchange={(e: dom.Event) => upload(e.target.asInstanceOf[Input])}></input>
        </div>
      </div>
    </div>
  }
}
