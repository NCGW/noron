package org.ncgw.noron.pages

import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.norn.shared.CommonProtocol.{ContentInputReq, ContentInputRsp}

import scala.language.postfixOps
import org.ncgw.noron.utils.{Http, JsFunc}
import org.ncgw.noron.Routes

import concurrent.ExecutionContext.Implicits.global
import org.scalajs.dom

/**
  * User: gaohan
  * Date: 2020/6/3
  * Time: 20:23
  */
object FinishPage {

  val status = false;


  def app: xml.Node = {
    println("123")
    <div>
      <div class="finish_content">
        <div class="finish_text">Congratulations！</div>
        <div class="finish_text">Today's Task Is Finished. </div>
        <div class="finish_text">Please Draw The ProgressBar. </div>
      </div>
      <div class="progess">
        <div class="box">
          <div class="bar" id="bar">
            <div class="line" id="line">
              <div class="dot" id="dot" onmouseover={()=> JsFunc.progressBar("bar", "line")}></div>
            </div>
          </div>
        </div>
      </div>
    </div>

  }

}
