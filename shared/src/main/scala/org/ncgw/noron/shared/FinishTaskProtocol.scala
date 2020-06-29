package org.ncgw.noron.shared

/**
  * User: gaohan
  * Date: 2020/6/25
  * Time: 17:38
  */
object FinishTaskProtocol {
  
  case class UpdateProgressReq(
    taskId: Long,
    progress: Int,
  ) extends Request
  
  case class UpdateRewardReq(
    useId: Long,
    reward: Int,
  ) extends Request


}
