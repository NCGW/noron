package org.ncgw.noron.http

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix}
import org.ncgw.noron.shared.RankProtocol._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
import org.ncgw.noron.models.dao.{RankDao}

import scala.concurrent.ExecutionContext.Implicits.global

trait RankService extends ServiceUtils with SessionBase{

  import io.circe._
  import io.circe.generic.auto._

  implicit val timeout: Timeout

  implicit val scheduler: Scheduler

  private val log = LoggerFactory.getLogger(getClass)


  private val getRank = (path("getRank") & get & pathEndOrSingleSlash) {
        dealFutureResult {
          RankDao.getRank.map { res =>
            if (res.isEmpty) {
              complete(ErrorRsp(200001, "rank is not exist"))
            } else {
              complete(GetRankRsp(res.map(i => rankInfo(i.userId, i.userName.get, i.coins.get)).toList.reverse))
            }
          }.recover {
            case e: Exception =>
              log.warn(s"get info error,${e.getMessage}")
              complete(ErrorRsp(2000031, "get rank error."))
          }
        }
  }

  private val getUserRank = (path("getUserRank") & post & pathEndOrSingleSlash) {
    entity(as[Either[Error, GetUserRankReq]]) {
      case Right(req) =>
        dealFutureResult {
          RankDao.getUserRank(req.userId).map { res =>
            if (res.isEmpty) {
              complete(ErrorRsp(200002, "user is not exist"))
            } else {
              dealFutureResult {
                RankDao.getRankCoin(req.userId).map { info =>
                  if (info.isEmpty) {
                    complete(ErrorRsp(200001, "rank is not exist"))
                  } else {
                    val userName = res.head.userName
                    val userIntegral = res.head.coins
                    val userRank = info.map(i => (i.userId == req.userId)).length
                    complete(GetUserRankRsp(userName.get,userRank,userIntegral.get))
                  }
                }.recover {
                  case e: Exception =>
                    log.warn(s"get info error,${e.getMessage}")
                    complete(ErrorRsp(2000031, "get rank error."))
                }
              }
            }
          }.recover {
            case e: Exception =>
              log.warn(s"get info error,${e.getMessage}")
              complete(ErrorRsp(2000032, "get info error."))
          }
        }

      case Left(error) =>
        log.warn(s"some error: $error")
        complete(ErrorRsp(100101,"parse error"))
    }
  }




  val rank: Route =
    pathPrefix("rank") {
      getRank ~ getUserRank
    }
}
