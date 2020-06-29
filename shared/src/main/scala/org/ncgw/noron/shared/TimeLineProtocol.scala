package org.ncgw.noron.shared

/**
  * Author: wmy
  * Date: 2020/06/25
  * Desc: 时间轴页面需要用到的和后台交互的协议
  */
object TimeLineProtocol {
  case class TimeLineItem(
                         taskId: Int,
                         startTime: Long,
                         endTime: Long,
                         content: String,
                         img: Option[String]
                         )

  case class TodayTasksRsp(
                          todayTasks: List[TimeLineItem],
                          errCode: Int = 0,
                          msg: String = "ok"
                          )extends Response

}
