package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.noron.shared.InputProtocol.{AddTaskReq, uploadSuccessRsp}
import org.ncgw.noron.shared.SpeechToTextProtocol._
import org.ncgw.noron.shared.SuccessRsp

import scala.language.postfixOps
import org.ncgw.noron.utils.{Http, JsFunc, TimeTool}
import org.ncgw.noron.Routes
import org.ncgw.noron.common.Constant

import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import org.scalajs.dom.document
import org.scalajs.dom.html.{Input, Select}
import org.scalajs.dom.raw.Event

import scala.scalajs.js.Date

class InputPage(userId : Long) {

  val fileName = Var("")
  val typeClass = Var("")

  def upload(input: Input): Unit = {
    val uploadName = input.value.split("\\\\").last
    JsFunc.alert("图片上传成功")
    fileName := "/noron/static/img/" + uploadName

  }

  def inputContent() : Unit = {

    val content = document.getElementById("content").asInstanceOf[Input].value
    val task = document.getElementById("choose").asInstanceOf[Select].value
    val taskType = task match {
      case "日程" => 0
      case "任务" => 1
      case "心愿" => 2
    }

    val startTime = task match {
      case "日程" => new Date(document.getElementById("startTime").asInstanceOf[Input].value).getTime().toLong - 8 * 60 * 60 * 1000
      case "任务" => document.getElementById("duringTime").asInstanceOf[Input].value.toLong
      case "心愿" => 0l
    }

    val endTime = task match {
      case "日程" => new Date(document.getElementById("endTime").asInstanceOf[Input].value).getTime().toLong - 8 * 60 * 60 * 1000
      case "任务" => new Date(document.getElementById("ddlTime").asInstanceOf[Input].value).getTime().toLong - 8 * 60 * 60 * 1000
      case "心愿" => 0l
    }

    if(content == "" || task == ""){
      JsFunc.alert("  请输入完整内容！")
    }else{
      val imgs = fileName.toString().drop(4).dropRight(1)
      val data = AddTaskReq(Constant.fakeUserId,startTime, endTime.toLong, content, taskType, imgs).asJson.noSpaces

      Http.postJsonAndParse[SuccessRsp](Routes.TaskList.addTask, data).map {
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

  def uploadContent() : Unit = {

    val task = document.getElementById("choose").asInstanceOf[Select].value
    val taskType = task match {
      case "日程" => 0
      case "任务" => 1
      case "心愿" => 2
    }

      val data = GetParseTextReq(taskType, "/Users/chenzhishuai/IdeaProjects/noron/audio/agenda1.wav").asJson.noSpaces

      Http.postJsonAndParse[GetParseTextRsp](Routes.ContentRoutes.getParseText, data).map {
        case Right(rsp) =>
          if (rsp.errCode == 0) {
            println("111" + rsp.startTime, rsp.endTime, rsp.duringTime, rsp.ddl, rsp.taskType, rsp.taskContent)
            if(rsp.taskType == 0){
              typeClass := "日程"
              document.getElementById("content").asInstanceOf[Input].value = rsp.taskContent
              document.getElementById("startTime").asInstanceOf[Input].value = TimeTool.DateFormatter(new Date(rsp.startTime.get), "yyyy-MM-dd hh:mm")
              document.getElementById("endTime").asInstanceOf[Input].value = TimeTool.DateFormatter(new Date(rsp.endTime.get), "yyyy-MM-dd hh:mm")
            }else if(rsp.taskType == 1){
              typeClass := "任务"
              document.getElementById("content").asInstanceOf[Input].value = rsp.taskContent
              document.getElementById("ddlTime").asInstanceOf[Input].value = TimeTool.DateFormatter(new Date(rsp.ddl.get), "yyyy-MM-dd hh:mm")
              document.getElementById("duringTime").asInstanceOf[Input].value = TimeTool.DateFormatter(new Date(rsp.duringTime.get), "yyyy-MM-dd hh:mm")
            }else {
              typeClass := "心愿"
              document.getElementById("content").asInstanceOf[Input].value = rsp.taskContent
            }
            JsFunc.alert("语音解析成功！")
            println("语音解析成功")
          } else {
            JsFunc.alert("任务上传失败！")
            println(rsp.msg)
          }
        case Left(e) =>
          println(s"get roomList with ${e}")
      }
  }

val start = Var(false)
  def app: xml.Node = {
//    dom.window.setInterval(() => typeClass := document.getElementById("type").asInstanceOf[Input].value, 300)

    <div>
      <div class="type">
        <div class="text-input">文字输入</div>{start.map {
        a =>
          <button class="voice-button" onclick={() =>
            if (a == false) {
              start := true; JsFunc.alert("开始录音"); JsFunc.startRecording()
            } else {
              start := false; JsFunc.alert("结束录音，请输入任务类型并提交")
            }}>
            <img src="/noron/static/img/语音输入.png" style="width:100px;"></img>
          </button>
      }}{fileName.map {
        img =>
          if (img == "") {
            <div class="upload">
              <img src="/noron/static/img/图片.png" style="width:84px; height:84px"></img>
              <input type="file" class="pic-input" name="file" onchange={(e: dom.Event) => upload(e.target.asInstanceOf[Input])}></input>
            </div>
          } else {
            <div class="upload">
              <img src={img} style="width:84px; height:84px"></img>
            </div>
          }
      }}
      </div>

      <div style="margin-left: 8%;">
        <div>
          <lable class="label-type">任务内容</lable>
          <input id="content" class="input-a"></input>
        </div>
        <div>
          <lable class="label-type">任务类型</lable>
          <select id="choose" class="input-a" onchange={(e: Event)=> typeClass := e.target.asInstanceOf[Select].value}>
            <option>选择任务类型</option>
            <option>日程</option>
            <option>任务</option>
            <option>心愿</option>
          </select>
        </div>
        {typeClass.map {
        t =>
          if (t == "日程") {
            <div>
              <div>
                <lable class="label-type">开始时间</lable>
                <input id="startTime" class="input-a" placeholder="正确格式：2020-07-06 10：00"></input>
              </div>
              <div>
                <lable class="label-type">结束时间</lable>
                <input id="endTime" class="input-a" placeholder="正确格式：2020-07-06 10：00"></input>
              </div>
            </div>
          }
          else if (t == "任务") {
            <div>
              <div>
                <lable class="label-type">截止时间</lable>
                <input id="ddlTime" class="input-a" placeholder="正确格式：2020-07-06 10：00"></input>
              </div>
              <div>
                <lable class="label-type">所需时间</lable>
                <input id="duringTime" class="input-a" placeholder="正确格式：4"></input>
              </div>
            </div>
          } else {
            <div></div>
          }
      }}
      </div>
      <div style="margin-top: 5%;text-align: center;">
        <button class="confirm" onclick={() => inputContent()}>上传文字</button>
        <button class="confirm" style="background: rgba(255, 64, 75, 1);" onclick={() => uploadContent()}>上传语音</button>
      </div>
      <div style="text-align: center;">
        <button class="confirm" style="background: rgba(22, 213, 133, 1);" onclick={() => inputContent()}>解析确认</button>
      </div>

    </div>
  }
}
