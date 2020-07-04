package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.noron.shared.InputProtocol.{AddTaskReq, uploadSuccessRsp}
import org.ncgw.noron.shared.SuccessRsp
import scala.language.postfixOps
import org.ncgw.noron.utils.Http
import org.ncgw.noron.Routes
import org.ncgw.noron.utils.JsFunc

import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.Input
import org.scalajs.dom.raw.{FileList, FormData}

class InputPage(userId : Long) {

  val fileName = Var("")

  def upload(input: Input): Unit = {
    val uploadName = input.value.split("\\\\").last
    JsFunc.alert("图片上传成功")
    fileName := "/noron/static/img/" + uploadName

  }

  def inputContent() : Unit = {

    val startTime = document.getElementById("startTime").asInstanceOf[Input].value
    val endTime = document.getElementById("endTime").asInstanceOf[Input].value
    val content = document.getElementById("content").asInstanceOf[Input].value
    val task = document.getElementById("type").asInstanceOf[Input].value
    val taskType = task match {
      case "日程" => 0
      case "任务" => 1
      case "心愿" => 2
    }

    if(content == "" || task == ""){
      JsFunc.alert("  请输入完整内容！")
    }else{
      val imgs = fileName.toString().drop(4).dropRight(1)
      val data = AddTaskReq(10001l,startTime.toLong, endTime.toLong, content, taskType, imgs).asJson.noSpaces

      Http.postJsonAndParse[SuccessRsp](Routes.ContentRoutes.addTask, data).map {
        case Right(rsp) =>
          if (rsp.errCode == 0) {
            JsFunc.alert("任务上传成功！")
            println("任务上传成功")
          } else {
            JsFunc.alert("任务上传失败！")
            println(rsp.msg)
          }

        case Left(e) =>
          println(s"get roomList with ${e}")

      }
    }

  }


  def app: xml.Node = {
    println("123")
    <div>
      <div class="type">
        <div class="text-input">文字输入</div>
        <button class="voice-button"><img src="/noron/static/img/语音输入.png" style="width:100px;"></img></button>
        {
        fileName.map{
          img =>
            if(img == ""){
              <div class="upload">
                <img src="/noron/static/img/图片.png" style="width:84px; height:84px"></img>
                <input type="file" class="pic-input" name="file" onchange={(e: dom.Event) =>upload(e.target.asInstanceOf[Input])}></input>
              </div>
            }else{
              <div class="upload">
                <img src={img} style="width:84px; height:84px"></img>
              </div>
            }
        }
        }
      </div>

      <div style="margin-left: 8%;">
        <div>
          <lable class="label-type">开始时间</lable>
          <input id="startTime" class="input-a" ></input>
        </div>
        <div>
          <lable class="label-type">结束时间</lable>
          <input id="endTime" class="input-a" ></input>
        </div>
        <div>
          <lable class="label-type">任务内容</lable>
          <input id="content" class="input-a" ></input>
        </div>
        <div>
          <lable class="label-type">任务类型</lable>
          <input id="type" class="input-a" ></input>
        </div>
      </div>
      <div class="plane">
        <div class="plane-time">上午九点</div>
        <div class="plane-todo">打卡</div>
        <div class="plane-todo">日程</div>
      </div>

      <div style="margin-top: 10%;text-align: center;">
        <button class="confirm" onclick={()=>inputContent()}>上传</button>
        <button class="confirm" style="background: rgba(255, 64, 75, 1);">确认</button>
      </div>
    </div>
  }
}
