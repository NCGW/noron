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

  val test = List(("上午九点","打卡","日程"),("上午十点","晨会","心愿"),("下午三点","汇报",""))
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
      <div class="type">
        <div class="text-input">文字输入</div>
        <button class="voice-button"><img src="/noron/static/img/语音输入.png" style="width:100px;"></img></button>
        <div class="upload">
          <img src="/noron/static/img/图片.png" style="width:84px; height:84px"></img>
          <input type="file" class="pic-input" name="file" onchange={(e: dom.Event) => upload(e.target.asInstanceOf[Input])}></input>
        </div>
      </div>

      <div style="margin-left: 10%;">
        <div>
          <lable class="label-type">时间</lable>
          <input id="shuru" class="input-a" ></input>
        </div>
        <div>
          <lable class="label-type">任务</lable>
          <input id="shuru" class="input-a" ></input>
        </div>
        <div>
          <lable class="label-type">类型</lable>
          <input id="shuru" class="input-a" ></input>
        </div>
      </div>
      <div class="plane">
        <div class="plane-time">上午九点</div>
        <div class="plane-todo">打卡</div>
        <div class="plane-todo">日程</div>
      </div>

      <div style="margin: 10%;text-align: center;">
        <button class="confirm">上传</button>
        <button class="confirm" style="background: #FF0000;">确认</button>
      </div>
    </div>
  }
}
