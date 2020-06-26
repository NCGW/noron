package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.norn.shared.CommonProtocol.{ContentInputReq, ContentInputRsp}
import org.ncgw.norn.shared.TaskStartProtocol.{GetTaskInfoReq, GetTaskInfoRsp, InfoClass}

import scala.language.postfixOps
import org.ncgw.noron.utils.{Http, JsFunc, TimeTool}
import org.ncgw.noron.{Index, Routes}

import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom

import scala.scalajs.js.Date

/**
  * User: gaohan
  * Date: 2020/6/3
  * Time: 20:23
  */
class FinishPage (TaskId: String) extends Index {

  val status = false;
  val taskId = TaskId.toLong


  val info = Var(List.empty[(InfoClass)])

  val testData = List(InfoClass(1,10001, "明天上午十点开会",100000l, 20000l, 30000l))

  val progress = Var(0.12)

  def getINFO(taskId: Long) = {
    val data_info = GetTaskInfoReq(taskId).asJson.noSpaces
    Http.postJsonAndParse[GetTaskInfoRsp](Routes.ContentRoutes.startContent, data_info).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          //info := rsp.infoData
          info := testData
        } else {
//          JsFunc.alert(rsp.msg)
//          dom.window.location.hash = s"#/Login"
//          println(rsp.msg)
          info := testData

        }
      case Left(error) =>
        println(s"get Start task error,$error")
        info := testData

    }
  }

  def updateProgress(taskId: Long, prg: Double)= {


  }

  def updateReward(taskId: Long, prg: Int) = {

  }

  def getProgress()= {
    val line = dom.document.getElementById("line").clientWidth.toDouble
    progress := (line / 800.00) + 0.03
    println("----"+ progress.toString())
  }

  def app: xml.Node = {
    getINFO(taskId);
    <div>
      {info.map {
      case Nil =>
        <div class="finish_content">
          <div class="finish_text" style="font-weight: 500;font-size: 55px;font-family: auto;">
            <p>
              <span>Task -
                {taskId}
              </span>
            </p>
          </div>

          <div class="finish_text">
            <p>
              <span>Task Content</span>
            </p>
          </div>
          <div class="finish_text" style="font-weight: 500;font-size: 55px;font-family: auto; margin-top: 150px; height: 250px">
            <p>
              <span>稍等……</span>
            </p>
          </div>
        </div>
      case list =>
        <div class="finish_content">
          <div class="finish_text" style="font-weight: 500;font-size: 55px;font-family: auto;" >
            <p>
              <span>
                {list.head.taskType match {
                case 1 => "AGENDA"
                case 2 => "ASSIGNMENT"
                case 3 => "WISH"
              }}
                -
                {taskId}
              </span>
            </p>
        </div>

        <div class="finish_text ">
          <p>
            <span>Task Content</span>
          </p>
        </div>

          <div class="finish_text" style="font-weight: 500;font-size: 55px;font-family: auto; margin-top: 150px; height: 250px">
            <p>
              <span>
                {list.head.taskContent}
              </span>
            </p>
          </div>

          <div class="finish_text " style="font-weight: 400;font-size: 40px;">
            <p>
              <span>
                Note: 请根据您的进度拖动动进度条至相应的位置，并提交
              </span>
            </p>
        </div>

        </div>
    }}
      <div class="progess">
        <div style="margin-top: 200px;">
          <p style="font-size: 40px; margin-left: 90px;">
            进度：{progress.map(p => (p * 100).toInt)}%
          </p>
        </div>
        <div class="box">
          <div class="bar" id="bar">
            <div class="line" id="line">
              <div class="dot" id="dot" onmouseover={() => {JsFunc.progressBar("bar", "line"); getProgress()}}></div>
            </div>
          </div>
        </div>
        <div class="finish_confirm" onclick={() => updateProgress(taskId, progress.toString().drop(4).dropRight(1).formatted("%s.2f").toDouble)}>
          <p style="font-size: 70px;text-align: center; padding-top: 17px; color:white">
            确  认  提  交
          </p>
        </div>
      </div>
    </div>


  }

}
