package org.ncgw.norn.shared

/**
  * User: WYN
  * Date: 2020/6/6
  * Time: 8:31
  */
object TaskStartProtocol {

  case class GetTaskInfoReq(
    taskid:Long
  )extends Request

  case class InfoClass(taskType:Int,taskID:Long,taskContent:String,startTime:Long,duringTime:Long,endTime:Long)
  case class GetTaskInfoRsp(
    infoData:List[(InfoClass)],
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response

  case class DelayReq(
    taskid:Long,
    mins_delay:Int
  )extends Request

  case class StartorCancleReq(
    taskid:Long,
    type_sorc:String
  )extends Request



}
