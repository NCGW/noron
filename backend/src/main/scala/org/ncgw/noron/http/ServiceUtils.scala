package org.ncgw.noron.http

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, HttpResponse}
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import org.slf4j.LoggerFactory
import org.seekloud.noron.protocols.ErrorRsp
import scala.concurrent.Future
import scala.util.{Failure, Success}


/**
  * User: Taoz
  * Date: 11/18/2016
  * Time: 7:57 PM
  */

object ServiceUtils{
  private val log = LoggerFactory.getLogger(this.getClass)

  case class CommonRsp(errCode: Int = 0, msg: String = "ok")

}

trait ServiceUtils {

  import ServiceUtils._
  import io.circe.generic.auto._
  import org.ncgw.noron.utils.CirceSupport._


  def htmlResponse(html: String): HttpResponse = {
    HttpResponse(entity = HttpEntity(ContentTypes.`text/html(UTF-8)`, html))
  }

  def jsonResponse(json: String): HttpResponse = {
    HttpResponse(entity = HttpEntity(ContentTypes.`application/json`, json))
  }

  def dealFutureResult(future: => Future[server.Route]): Route = {
    onComplete(future) {
      case Success(rst) => rst
      case Failure(e) =>
        e.printStackTrace()
        log.error("internal error: {}", e.getMessage)
        complete(ErrorRsp(1000, "internal error."))
    }
  }

}
