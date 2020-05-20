package org.ncgw.noron.http

import akka.actor.ActorSystem
import akka.event.LoggingAdapter
import akka.http.scaladsl.model.headers.CacheDirectives.{`max-age`, public}
import akka.http.scaladsl.model.headers.{CacheDirectives, `Cache-Control`}
import akka.http.scaladsl.server.Directives.{pathPrefix, _}
import akka.http.scaladsl.server.{Directive0, Route}
import akka.http.scaladsl.server.directives.ContentTypeResolver.Default
import akka.stream.Materializer
import org.ncgw.noron.common.AppSettings._

import scala.concurrent.ExecutionContextExecutor

/**
  * User: Taoz
  * Date: 11/16/2016
  * Time: 10:37 PM
  *
  * 12/09/2016:   add response compress. by zhangtao
  * 12/09/2016:   add cache support self. by zhangtao
  *
  */
trait ResourceService {

  implicit val system: ActorSystem

  implicit val executor: ExecutionContextExecutor

  implicit val materializer: Materializer

  val log: LoggingAdapter


  private val resources = {
    pathPrefix("html") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("html")
      }
    } ~ pathPrefix("css") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("css")
      }
    } ~
    pathPrefix("js") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("js")
      }
    } ~
    pathPrefix("sjsout") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("sjsout")
      }
    } ~
    pathPrefix("img") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("img")
      }
    }  ~
    pathPrefix("fonts") {
      extractUnmatchedPath { _ =>
        getFromResourceDirectory("fonts")
      }
    }

  }

  //只使用强制缓存,设置不缓存，去除协商缓存字段
  def setNoCacheInHeaderWithFilter: Directive0 = {
    mapResponseHeaders { headers =>
      `Cache-Control`.apply(CacheDirectives.`no-cache`) +: headers.filterNot(h => h.name() == "Last-Modified" || h.name() == "ETag")
    }
  }

  //cache code copied from zhaorui.
  private val cacheSeconds = 24 * 60 * 60

  def resourceRoutes: Route = (pathPrefix("static") & get) {
    extractUnmatchedPath { name =>
      mapResponseHeaders { headers => `Cache-Control`(`public`, `max-age`(cacheSeconds)) +: headers } {
        encodeResponse(resources)
      }
    }

  }


}
