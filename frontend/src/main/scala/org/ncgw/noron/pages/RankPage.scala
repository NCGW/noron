package org.ncgw.noron.pages

import org.ncgw.noron.Index
import mhtml._
import io.circe.generic.auto._
import io.circe.syntax._
import org.ncgw.norn.shared.RankProtocol._
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

  val rankList = Var(List.empty[rankInfo])
//  val list = Var(List((10001,"gh",100),(10002,"shuai",90),(10003,"nannan",80),(10004,"yuan",70)))

  val userName = Var("")
  val userRank = Var(0)
  val userIntegral = Var(0)

  def getRank(): Unit = {
    Http.getAndParse[GetRankRsp](Routes.Rank.getRank).map {
      case Right(rsp) =>
        if(rsp.errCode == 0) {
          rankList := rsp.rankData
        } else {
          JsFunc.alert("获取积分列表失败！")
          println(s"error${rsp.errCode}:${rsp.msg}")
        }
      case Left(error) =>
        println("error======" + error)
    }
  }

  def getUserRank() : Unit ={

    val data_info = GetUserRankReq(10002l).asJson.noSpaces
    Http.postJsonAndParse[GetUserRankRsp](Routes.Rank.getUserRank, data_info).map {
      case Right(rsp) =>
        if (rsp.errCode == 0) {
          userName := rsp.userName
          userRank := rsp.userRank
          userIntegral := rsp.userIntegral
        } else {
          JsFunc.alert("获取个人积分排行失败！")
          println(rsp.msg)
        }
      case Left(error) =>
        println(s"get user rank error,$error")

    }

  }

  def app: xml.Node = {
    getRank()
    getUserRank()

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
        <div class="userName">{userName.map(r => r)}</div>
        <div class="pai-ming">积分为：{userIntegral.map(r => r)}，排名第{userRank.map(r => r)}</div>
      </div>
      <div class="ge-li"></div>
      {
      rankList.map{
        res =>
          res.map{
            l =>
              <div class="hang">
                <div class="rank-userId">{l.userId}</div>
                <div class="rank-userName">{l.userName}</div>
                <div class="rank-integral">{l.integral}</div>
              </div>
          }
      }
      }
    </div>
  }

}
