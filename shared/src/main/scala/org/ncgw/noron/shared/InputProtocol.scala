package org.ncgw.noron.shared

object InputProtocol {

  case class AddTaskReq(
                        userId: Long,
                        startTime: Long,
                        endTime: Long,
                        content: String,
                        taskType: Int,
                        img: String
                   ) extends Request

  case class uploadSuccessRsp(
                               fileUrl:String,
                               fileName:String,
                               errCode:Int=0,
                               msg:String="ok"
                             ) extends Response

}
