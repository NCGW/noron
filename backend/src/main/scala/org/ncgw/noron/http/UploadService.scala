//package org.ncgw.noron.http
//
//import akka.http.scaladsl.server.Directives.{complete, fileUpload, parameters, path, pathPrefix, withoutSizeLimit}
//import akka.http.scaladsl.server.{Directive1, Route}
//import org.ncgw.noron.shared.InputProtocol._
//import akka.http.scaladsl.server.Route
//import akka.http.scaladsl.server.Directives._
//import akka.actor.Scheduler
//import akka.stream.scaladsl.{FileIO, Source}
//import akka.util.{ByteString, Timeout}
//import org.slf4j.LoggerFactory
//import org.ncgw.noron.shared.{ErrorRsp, SuccessRsp}
//import org.ncgw.noron.models.dao.RankDao
//
//import scala.concurrent.ExecutionContext.Implicits.global
//import scala.util.{Failure, Success}
//
//trait UploadService extends ServiceUtils with SessionBase{
//
//  import io.circe._
//  import io.circe.generic.auto._
//
//  implicit val timeout: Timeout
//
//  implicit val scheduler: Scheduler
//
//  private val log = LoggerFactory.getLogger(getClass)
//
//  private def storeFile(source: Source[ByteString, Any]): Directive1[java.io.File] = {
//    val dest = java.io.File.createTempFile("leaf", ".tmp")
//    val file = source.runWith(FileIO.toPath(dest.toPath)).map(_ => dest)
//    onComplete[java.io.File](file).flatMap {
//      case Success(f) =>
//        provide(f)
//      case Failure(e) =>
//        dest.deleteOnExit()
//        failWith(e)
//    }
//  }
//
//  private def uploadFile = (path("uploadFile") & post) {
////      parameters('targetDir.as[String]) {
//        (targetDir) =>
//              fileUpload("fileUpload") {
//                case (fileInfo, file) =>
//                  storeFile(file) { f =>
//                    dealFutureResult {
//                      HestiaClient.upload(f, fileInfo.fileName).map {
//                        case Right(url) =>
//                          f.deleteOnExit()
//                          log.debug(s"${fileInfo.fileName} + ${fileInfo.fieldName} + ${fileInfo.contentType} upload success ")
//                          complete(uploadSuccessRsp(url, fileInfo.fileName))
//
//                        case Left(error) =>
//                          f.deleteOnExit()
//                          log.debug(s"upload error111")
//                          complete(ErrorRsp(100100, "upload error!!"))
//                      }
//                    }
//                  }
//              }
////      }
//  }
//
//  val rank: Route =
//    pathPrefix("upload") {
//      uploadFile
//    }
//}
