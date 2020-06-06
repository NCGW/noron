package org.ncgw.norn.shared


object CommonProtocol {

  object RoomSate{
    val ready: Byte = 0
    val on: Byte = 1
  }

  case class ContentInputReq(
                        userId: Long,
                        content: String,
                   ) extends Request

  case class ContentInputRsp(

                        errCode: Int = 0,
                        msg: String = "ok"
                      ) extends Response

  case class UploadPicReq(
                           userId: Long,
                           picName: String
                         )extends Request

}
