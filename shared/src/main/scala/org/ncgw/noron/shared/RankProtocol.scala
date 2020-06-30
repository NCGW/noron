package org.ncgw.noron.shared

object RankProtocol {

  case class rankInfo(
                     userId: Long,
                     userName: String,
                     integral: Int
                     )

  case class GetRankRsp(
                         rankData:List[(rankInfo)],
                         errCode: Int = 0,
                         msg: String = "ok"
                       ) extends Response

  case class GetUserRankReq(
                           userId : Long
                           )extends Request

  case class GetUserRankRsp(
                             userName: String,
                             userRank: Int,
                             userIntegral: Int,
                             errCode: Int = 0,
                             msg: String = "ok"
                           )extends Response

}
