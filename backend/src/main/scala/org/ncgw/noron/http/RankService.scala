package org.ncgw.noron.http

import akka.http.scaladsl.server.Directives.{as, complete, entity, path, pathPrefix}
import org.ncgw.norn.shared.RankProtocol._
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._
import akka.actor.Scheduler
import akka.util.Timeout
import org.slf4j.LoggerFactory
import org.ncgw.norn.shared.{ErrorRsp, SuccessRsp}
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
              complete(ErrorRsp(200102, "rank is not exist"))
            } else {
              complete(GetRankRsp(res.map(i => rankInfo(i.userId, i.userName.get, i.coins.get)).toList))
            }
          }.recover {
            case e: Exception =>
              log.warn(s"get info error,${e.getMessage}")
              complete(ErrorRsp(2000031, "get rank error."))
          }
        }
  }




  val rank: Route =
    pathPrefix("rank") {
      getRank
    }
}
