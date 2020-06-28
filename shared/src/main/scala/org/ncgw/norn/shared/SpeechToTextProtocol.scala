package org.ncgw.norn.shared

/**
  * User: gaohan
  * Date: 2020/6/27
  * Time: 16:55
  */
object SpeechToTextProtocol {


  case class GetParseTextReq(
    taskType: Int, //1： 日程， 2： 任务， 3： 心愿
    url: String,
  ) extends Request

  case class GetParseTextRsp(
    taskType: Int, //1： 日程， 2： 任务， 3： 心愿
    taskContent: String,
    startTime: Option[Long],
    endTime: Option[Long],
    duringTime: Option[Long],
    ddl: Option[Long],
    errCode: Int = 0,
    msg: String = "ok"
  ) extends Response



}
