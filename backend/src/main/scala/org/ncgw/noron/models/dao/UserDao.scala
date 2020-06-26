package org.ncgw.noron.models.dao

import org.ncgw.norn.shared.TaskStartProtocol.InfoClass
import org.ncgw.noron.models.SlickTables
import slick.jdbc.PostgresProfile.api._
import org.ncgw.noron.utils.DBUtil.db
import org.slf4j.LoggerFactory

import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
/**
  * User: gaohan
  * Date: 2020/6/26
  * Time: 16:26
  */
object UserDao {

  private val log = LoggerFactory.getLogger(this.getClass)
  private val tUser = SlickTables.tUser

  def updateReward(userId: Long, reward: Option[Int])= db.run{
    tUser.filter(_.userId === userId).map(c => c.coins).update(reward)
  }

  def getReward(userId: Long) = db.run{
    tUser.filter(_.userId === userId).map(_.coins).result
  }



}
