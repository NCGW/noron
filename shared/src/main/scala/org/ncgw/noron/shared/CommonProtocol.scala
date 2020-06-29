package org.ncgw.noron.shared

object CommonProtocol {

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
