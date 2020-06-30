package org.ncgw.noron.pages

/**
  * User: WYN
  * Date: 2020/6/5
  * Time: 17:21
  */
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.noron.shared.TaskStartProtocol._
import org.ncgw.noron.shared.{CommonRsp, ErrorRsp, SuccessRsp}

import scala.language.postfixOps
import org.ncgw.noron.utils.{Http, JsFunc, TimeTool}
import org.ncgw.noron.Routes

import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom
import org.ncgw.noron.Index

import scala.concurrent.Future
import scala.scalajs.js.Date


class StartPage(Taskid: String) extends Index {
  val taskid=Taskid.toLong
//  JsFunc.alert(taskid.toString)

  def delay(min:Int)={
    val delay = DelayReq(taskid,min).asJson.noSpaces
    Http.postJsonAndParse[CommonRsp](Routes.Start.delay, delay).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          JsFunc.alert("延时成功！")
        } else {
          JsFunc.alert("延时失败！")
          println(rsp.msg)
        }
      case Left(error) =>
        JsFunc.alert("延时失败2！")
        println(s"parse error,$error")
    }
    dom.window.location.hash = s"#/Start/$taskid"
  }

  def start(type_sorc:String)={
    val type_sc = StartorCancleReq(taskid,type_sorc).asJson.noSpaces
    Http.postJsonAndParse[CommonRsp](Routes.Start.startorcancel, type_sc).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
//          JsFunc.alert(rsp.msg)
          JsFunc.alert("特殊时期，出门请戴口罩，避免人多聚集")
        } else {
          JsFunc.alert("失败！")
          println(rsp.msg)
        }
      case Left(error) =>
        JsFunc.alert("失败2！")
        println(s"parse error,$error")
    }
    dom.window.location.hash = s"#/Start/$taskid"
  }


  val info = Var(List.empty[(InfoClass)])
  def getINFO(taskid: Long) = {
    val data_info = GetTaskInfoReq(taskid).asJson.noSpaces
//    JsFunc.alert(data_info.toString)
    Http.postJsonAndParse[GetTaskInfoRsp](Routes.Start.getInfo, data_info).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          info := rsp.infoData
        } else {
          JsFunc.alert(rsp.msg)
          dom.window.location.hash = s"#/Login"
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get Start task error,$error")
    }
  }

  val INFO = info.map {
    case Nil =>
      <div id="u3" class="ax_default box_2">
        <!-- <div id="u3_div" > -->
        <!-- Unnamed (Rectangle) -->
        <div id="u6" class="ax_default label">
          <div id="u6_div" ></div>
          <div id="u6_text" class="text">
            <p><span>Task - {taskid}</span></p>
          </div>
        </div>

        <!-- Unnamed (Rectangle) -->
        <div id="u4" class="ax_default label">
          <div id="u4_div" ></div>
          <div id="u4_text" class="text ">
            <p><span>Task Content</span></p>
          </div>
        </div>

        <!-- Unnamed (Rectangle) -->
        <div id="u7" class="ax_default label">
          <div id="u7_div" ></div>
          <div id="u7_text" class="text ">
            <p><span>稍等……</span></p>
          </div>
        </div>

        <!-- Unnamed (Rectangle) -->
        <div id="u5" class="ax_default label">
          <div id="u5_div" ></div>
          <div id="u5_text" class="text ">
            <p><span>Schedule Time</span></p>
          </div>
        </div>

        <div id="u3_text" class="text " style="display:none; visibility: hidden">
        </div>
        <!-- </div> -->
      </div>

    case list => <div>
          {list.map {l =>
        <div id="u3" class="ax_default box_2">
          <!-- <div id="u3_div" > -->
          <!-- Unnamed (Rectangle) -->
          <div id="u6" class="ax_default label">
            <div id="u6_div" ></div>
            <div id="u6_text" class="text">
              <p><span>{l.taskType match{
                case 1=> "AGENDA"
                case 2=> "ASSIGNMENT"
                case 3=> "WISH"}} - {taskid}</span></p>
            </div>
          </div>

          <!-- Unnamed (Rectangle) -->
          <div id="u4" class="ax_default label">
            <div id="u4_div" ></div>
            <div id="u4_text" class="text ">
              <p><span>Task Content</span></p>
            </div>
          </div>

          <!-- Unnamed (Rectangle) -->
          <div id="u7" class="ax_default label">
            <div id="u7_div" ></div>
            <div id="u7_text" class="text ">
              <p><span>{l.taskContent}</span></p>
            </div>
          </div>

          <!-- Unnamed (Rectangle) -->
          <div id="u5" class="ax_default label">
            <div id="u5_div" ></div>
            <div id="u5_text" class="text ">
              <p><span>Schedule Time</span></p>
            </div>
          </div>

          <div id="time">
            <div id="u8" class="ax_default label">
              <div id="u8_div" ></div>
              <div id="u8_text" class="text ">
                <p><span>{TimeTool.dateFormatDefault(l.startTime)}</span></p>
              </div>
            </div>

            <div id="duringtime">
              <!-- Unnamed (Rectangle) -->
              <div id="u11" class="ax_default label">
                <div id="u11_div" ></div>
                <div id="u11_text" class="text ">
                  <p><span>
                    {((new Date(l.duringTime)).getDate*24+(new Date(l.duringTime)).getHours-32) match{
                      case 0=>""
                      case other=>other.toString+"h"}}
                    {(new Date(l.duringTime)).getMinutes() match{
                      case 0=>""
                      case other=>other.toString+"m"}}
                    {(new Date(l.duringTime)).getSeconds() match{
                      case 0=>""
                      case other=>other.toString+"s"}}
                  </span></p>
                </div>
              </div>

              <!-- Unnamed (Line) -->
              <div id="u10" class="ax_default line">
                <div id="u10_div" ></div>
                <div id="u10_text" class="text " style="display:none; visibility: hidden">
                  <p></p>
                </div>
              </div>

            </div>
            <!-- Unnamed (Rectangle) -->
            <div id="u9" class="ax_default label">
              <div id="u9_div" ></div>
              <div id="u9_text" class="text ">
                <p><span>{TimeTool.dateFormatDefault(l.endTime)}</span></p>
              </div>
            </div>
          </div>

          <div id="u3_text" class="text " style="display:none; visibility: hidden">
          </div>
          <!-- </div> -->
        </div>
          }
        }
    </div>
  }

  def app: xml.Node = {
//    println("123")
    getINFO(taskid)
    <div id="base" >
      {INFO}
      <div id="u2" class="ax_default primary_button" onclick={()=>start("start")}>
        <div id="u2_div" ></div>
        <div id="u2_text" class="text ">
          <p><span>Start Now</span></p>
        </div>
      </div>

      <div id="u0" class="ax_default primary_button" onclick={()=>delay(5)}>
        <div id="u0_div" ></div>
        <div id="u0_text" class="text ">
          <p><span>Delay 5 mins</span></p>
        </div>
      </div>

      <!-- Unnamed (Rectangle) -->
      <div id="u1" class="ax_default primary_button" onclick={()=>start("cancle")}>
        <div id="u1_div" ></div>
        <div id="u1_text" class="text ">
          <p><span>Cancle</span></p>
        </div>
      </div>

    </div>


  }
}

