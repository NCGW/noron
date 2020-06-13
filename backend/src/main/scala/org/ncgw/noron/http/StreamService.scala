package org.ncgw.noron.http

import akka.http.scaladsl.server.Route
import akka.http.scaladsl.server.Directives._


/**
  * User: wmy
  * Date: 2020/3/20
  * Desc: 用户推流和拉流相关接口
  * */
trait StreamService extends ServiceUtils with SessionBase {

  import io.circe._
  import io.circe.syntax._
  import io.circe.generic.auto._

}
