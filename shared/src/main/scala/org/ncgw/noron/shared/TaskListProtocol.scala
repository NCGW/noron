package org.ncgw.noron.shared

object TaskListProtocol {

  case class TaskItem(
                       taskId: Long,
                       startTime: Long,
                       endTime: Long,
                       content: String,
                       img: Option[String],
                       taskType: Int,
                       taskProgress: Int,
                       priority: Int
                     )

  case class TaskListRsp(
                        taskList: List[TaskItem],
                        override val errCode: Int = 0,
                        override val msg: String = "ok"
                        ) extends Response

}
