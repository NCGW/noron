package org.ncgw.noron.pages

import org.ncgw.noron.Index
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

class RankPage(userId: Long) extends Index {

  val list = Var(List((10001,"gh",100),(10002,"shuai",90),(10003,"nannan",80),(10004,"yuan",70)))

  def app: xml.Node = {
    <div>

      <div class="my-swiper">
        <ul class="swiper-list">
          <li class="swiper-slide swiper-slide1">
            <a href="javascript:;">
              <img src="/noron/static/img/1.jpg"></img>
            </a>
          </li>
          <li class="swiper-slide swiper-slide2">
            <a href="javascript:;">
              <img src="/noron/static/img/2.jpg"></img>
            </a>
          </li>
          <li class="swiper-slide swiper-slide3">
            <a href="javascript:;">
              <img src="/noron/static/img/3.jpg"></img>
            </a>
          </li>
          <li class="swiper-slide swiper-slide4">
            <a href="javascript:;">
              <img src="/noron/static/img/4.jpg"></img>
            </a>
          </li>
          <li class="swiper-slide swiper-slide5">
            <a href="javascript:;">
              <img src="/noron/static/img/5.jpg"></img>
            </a>
          </li>
        </ul>
      </div>

      <div>
        <div class="userName">shuai</div>
        <div class="pai-ming">积分为：90，排名第2</div>
      </div>
      <div class="ge-li"></div>
      {
      list.map{
        res =>
          res.map{
            l =>
              <div class="hang">
                <div class="rank-userId">{l._1}</div>
                <div class="rank-userName">{l._2}</div>
                <div class="rank-integral">{l._3}</div>
              </div>
          }
      }
      }
    </div>
  }

}
