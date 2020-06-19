package org.ncgw.norn.shared

/**
  * User: WYN
  * Date: 2020/6/6
  * Time: 8:31
  */
object TaskStartProtocol {

  case class GetTaskInfoReq(
    taskid:String
  )extends Request
  case class InfoClass(taskType:String,taskID:String,taskContent:String,startTime:Double,duringTime:Double,endTime:Double)
  case class GetTaskInfoRsp(
    infoData:InfoClass,
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response


  case class DelayReq(
    mins_delay:Int
  )extends Request

  case class StartorCancleReq(
    type_sorc:String
  )extends Request




}
