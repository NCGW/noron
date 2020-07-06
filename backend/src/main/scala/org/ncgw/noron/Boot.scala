package org.ncgw.noron

import akka.actor.typed.ActorRef
import akka.actor.{ActorSystem, Scheduler}
import akka.dispatch.MessageDispatcher
import akka.event.{Logging, LoggingAdapter}
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import akka.util.Timeout

import scala.util.{Failure, Success}
import org.ncgw.noron.common.AppSettings._
import org.ncgw.noron.http.HttpService

import concurrent.duration._

/**
  * User: Taoz
  * Date: 11/16/2016
  * Time: 1:00 AM
  */

object Boot extends HttpService {

  implicit val system: ActorSystem = ActorSystem("noron", config)

  override implicit val executor: MessageDispatcher =
    system.dispatchers.lookup("akka.actor.my-blocking-dispatcher")

  override implicit val materializer: ActorMaterializer = ActorMaterializer()

  override implicit val scheduler: Scheduler = system.scheduler

  override implicit val timeout: Timeout = Timeout(20 seconds) // for actor asks

  val log: LoggingAdapter = Logging(system, getClass)


  def main(args: Array[String]): Unit = {
    log.info("Starting.")
    val binding = Http().bindAndHandle(routes, httpInterface, httpPort)
    binding.onComplete {
      case Success(b) ⇒
        val localAddress = b.localAddress
        println(s"Server is listening on ${localAddress.getHostName}:${localAddress.getPort}")
        system.scheduler.schedule(0 seconds, 1 days) {
          models.dao.PRIDAO.renewPRI
        }
        system.scheduler.schedule(10 seconds, 1 days) {
          utils.PRIUtil.priority
        }
      case Failure(e) ⇒
        println(s"Binding failed with ${e.getMessage}")
        system.terminate()
        System.exit(-1)
    }
  }
}
