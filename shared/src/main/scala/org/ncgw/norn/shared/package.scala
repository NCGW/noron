package org.ncgw.norn

/**
  * Author: Administrator
  * Date: 2020/3/23/023
  * Time: 16:05
  */
package object shared {

  trait Request

  trait Response {
    val errCode: Int
    val msg: String
  }

  case class CommonRsp(
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response

  def ErrorRsp(errCode:Int, msg: String) = CommonRsp(errCode, msg)

  final case class SuccessRsp(
                               errCode: Int = 0,
                               msg: String = "ok"
                             ) extends Response
}
