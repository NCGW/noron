package org.ncgw.noron

/**
  * User: wmy
  * Date: 2020/3/20
  * Desc: commonRsp
  * */
package object protocols {
  trait CommonRsp{
    val errCode: Int
    val msg: String
  }

  final case class ErrorRsp(
                             errCode: Int,
                             msg: String
                           ) extends CommonRsp

  final case class SuccessRsp(
                               errCode: Int = 0,
                               msg: String = "ok"
                             ) extends CommonRsp


  val parseError=ErrorRsp(100101,"parse error")
}
