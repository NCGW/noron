package org.ncgw.noron.http

import akka.http.scaladsl.model._
import akka.http.scaladsl.server
import akka.http.scaladsl.server.Directives.{complete, extractRequestContext, redirect}
import akka.http.scaladsl.server.directives.BasicDirectives
import akka.http.scaladsl.server.{Directive0, Directive1}
import io.circe.syntax._
import org.ncgw.noron.common.AppSettings
import org.ncgw.noron.utils.{CirceSupport, SessionSupport}
import org.seekloud.noron.common.AppSettings
import org.seekloud.noron.utils.{CirceSupport, SessionSupport}
import org.slf4j.LoggerFactory

/**
  * User: Taoz
  * Date: 12/4/2016
  * Time: 7:57 PM
  */
object SessionBase extends CirceSupport{

  val SessionTypeKey = "STKey"
  private val logger = LoggerFactory.getLogger(this.getClass)

  object UserSessionKey {
    val SESSION_TYPE = "userSession"
    val token = "token"
    val createTime = "createTime"
    val expires = "expires"
    val enterpriseId = "enterpriseId"
    val userId = "userId"
    val userName = "userName"
    val loginTime = "loginTime"
    val canAccessKnowLedge = "canAccessKnowLedge"
    val canAccessPortal = "canAccessPortal"
    val userType = "userType"
  }

  case class UserSession(
    token: String,
    createTime: Long,
    expires: Long,
    enterpriseId: String,
    userId: String,
    userName: String,
    loginTime: Long,
    canAccessKnowLedge: Boolean,
    canAccessPortal: Boolean,
    userType: String

  ) {
    def toSessionMap = Map(
      SessionTypeKey -> UserSessionKey.SESSION_TYPE,
      UserSessionKey.token -> token,
      UserSessionKey.createTime -> createTime.toString,
      UserSessionKey.expires -> expires.toString,
      UserSessionKey.enterpriseId -> enterpriseId,
      UserSessionKey.userId -> userId,
      UserSessionKey.userName -> userName,
      UserSessionKey.loginTime -> loginTime.toString,
      UserSessionKey.canAccessKnowLedge -> canAccessKnowLedge.toString,
      UserSessionKey.canAccessPortal -> canAccessPortal.toString,
      UserSessionKey.userType -> userType
    )
  }

  implicit class SessionTransformer(sessionMap: Map[String, String]) {
    def toUserSession: Option[UserSession] = {
      logger.debug(s"toUserSession: change map to session, ${sessionMap.mkString(",")}")
      try {
        if (sessionMap.get(SessionTypeKey).exists(_.equals(UserSessionKey.SESSION_TYPE))) {
          Some(UserSession(
            sessionMap(UserSessionKey.token),
            sessionMap(UserSessionKey.createTime).toLong,
            sessionMap(UserSessionKey.expires).toLong,
            sessionMap(UserSessionKey.enterpriseId),
            sessionMap(UserSessionKey.userId),
            sessionMap(UserSessionKey.userName),
            sessionMap(UserSessionKey.loginTime).toLong,
            sessionMap(UserSessionKey.canAccessKnowLedge).toBoolean,
            sessionMap(UserSessionKey.canAccessPortal).toBoolean,
            sessionMap(UserSessionKey.userType)
          ))
        } else {
          logger.debug("no session type in the session")
          None
        }
      } catch {
        case e: Exception =>
          e.printStackTrace()
          logger.warn(s"toUserSession: ${e.getMessage}")
          None
      }
    }
  }

}

trait SessionBase extends CirceSupport with SessionSupport {

  import SessionBase._
  import io.circe.generic.auto._

  override val sessionEncoder = SessionSupport.PlaySessionEncoder
  override val sessionConfig = AppSettings.sessionConfig

  protected def setUserSession(userSession: UserSession): Directive0 = setSession(userSession.toSessionMap)

  protected val optionalUserSession: Directive1[Option[UserSession]] = optionalSession.flatMap {
    case Right(sessionMap) => BasicDirectives.provide(sessionMap.toUserSession)
    case Left(error) =>
      logger.debug(error)
      BasicDirectives.provide(None)
  }

  def authUser(f: UserSession => server.Route) = optionalUserSession {
    case Some(session) =>
      f(session)
    case None =>
      redirect("/",StatusCodes.SeeOther)
  }

}