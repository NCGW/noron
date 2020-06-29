package org.ncgw.noron.models.dao

import org.ncgw.noron.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global

object RankDao {

  private val log = LoggerFactory.getLogger(this.getClass)
  private val tUser = SlickTables.tUser

  def getRank = db.run{
    tUser.sortBy(_.coins).result
  }

  def getUserRank (userId : Long) = db.run{
    tUser.filter(_.userId === userId).result
  }

  def getRankCoin(userId : Long)=db.run{
    tUser.filter(_.userId === userId).sortBy(_.coins.desc).result
  }

}
