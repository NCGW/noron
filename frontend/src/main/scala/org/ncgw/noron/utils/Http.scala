package org.ncgw.noron.utils

import io.circe.{Decoder, Error}
import org.scalajs.dom
import org.scalajs.dom.experimental._
import org.scalajs.dom.raw.{FileReader, FormData}

import scala.concurrent.Future

/**
  * User: Taoz
  * Date: 11/29/2016
  * Time: 9:41 PM
  */
object Http {

  import concurrent.ExecutionContext.Implicits.global

  lazy val jsonPostHeader = {
    try {
      val h = new Headers()
      h.append("Content-Type", "application/json")
      h
    } catch {
      case e: Exception =>
        val errMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"jsHeader errMsg: $errMsg")
        throw e
    }
  }

  lazy val formPostHeader = {
    try {
      val h = new Headers()
      h.append("Content-Type", "multipart/form-data;boundary=--------")
      h
    } catch {
      case e: Exception =>
        val errMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"jsHeader errMsg: $errMsg")
        throw e
    }
  }

  def postJson(url: String, bodyStr: String, withCookie: Boolean = true): Future[String] = {
    println(s"sendJsonPost: url=$url body=$bodyStr header=$jsonPostHeader")
    try {
      val requestInit = RequestInit(
        method = HttpMethod.POST,
        body = bodyStr,
        headers = jsonPostHeader,
        credentials = RequestCredentials.`same-origin`
        //        credentials = RequestCredentials.include,
        //        mode = RequestMode.`no-cors`
      )
      dom.experimental.Fetch.fetch(
        url, requestInit
      ).toFuture.flatMap { r =>
        //        println(s"msg sent to $url: $bodyStr");
        r.text().toFuture
      }
    } catch {
      case e: Exception =>
        val errorDetailMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"sendJsonPost errMsg: $errorDetailMsg")
        throw e
    }

  }

  def postFormData(url: String, bodyStr: FormData, withCookie: Boolean = true): Future[String] = {
    println(s"sendJsonPost: url=$url body=$bodyStr header=$formPostHeader")
    try {
      val requestInit = RequestInit(
        method = HttpMethod.POST,
        body = bodyStr,
        //        headers = formPostHeader,
        credentials = RequestCredentials.`same-origin`
        //        credentials = RequestCredentials.include,
        //        mode = RequestMode.`no-cors`
      )
      dom.experimental.Fetch.fetch(
        url, requestInit
      ).toFuture.flatMap { r =>
        //println(s"msg sent to $url: $bodyStr,,${r.text()},,,${r.formData()}");
        r.text().toFuture
      }
    } catch {
      case e: Exception =>
        val errorDetailMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"sendJsonPost errMsg: $errorDetailMsg")
        throw e
    }

  }

  def postJsonAndParse[T](
                           url: String,
                           bodyStr: String,
                           withCookie: Boolean = true)(implicit decoder: Decoder[T]): Future[Either[Error, T]] = {
    import io.circe.parser._
    postJson(url, bodyStr, withCookie).map{s =>
      println(url, s)
      decode[T](s)
    }
  }

  def postFormAndParse[T](
                           url: String,
                           bodyStr: FormData,
                           withCookie: Boolean = true)(implicit decoder: Decoder[T]): Future[Either[Error, T]] = {
    import io.circe.parser._
    postFormData(url, bodyStr, withCookie).map(s => {println("ssss is",s);decode[T](s) })
  }



  def get(url: String, withCookie: Boolean = true): Future[String] = {
    try {
      val requestInit = RequestInit(
        method = HttpMethod.GET,
        credentials = RequestCredentials.`same-origin`,
        //        credentials = RequestCredentials.include,
        mode = RequestMode.cors
      )
      dom.experimental.Fetch.fetch(
        url, requestInit
      ).toFuture.flatMap { r =>
        //        println(s"msg sent to $url")
        r.text().toFuture
      }.map { rst =>
        //        println(s"rst got: $rst")
        rst
      }
    } catch {
      case e: Exception =>
        val errorDetailMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"sendGet errMsg: $errorDetailMsg")
        throw e
    }

  }

  def getWithoutReferrer(url: String, withCookie: Boolean = false): Future[String] = {
    try {
      val requestInit = RequestInit(
        method = HttpMethod.GET,
        credentials = RequestCredentials.`same-origin`
      )
      dom.experimental.Fetch.fetch(
        url, requestInit
      ).toFuture.map { i =>
        val file=new FileReader()
        println(s"i.status===${i.status}")
        println(s"i.status text===${i.statusText}")
        println(s"i.ok===${i.ok}")
        println(s"i.type===${i.`type`}")
        println(s"i.url=====${i.url}")
        println(s"i.text=====${i.text()}")
        println(s"i.arrayBuffer=====${i.arrayBuffer()}")
        i.arrayBuffer().toFuture.map { i =>
          println(s"i.arrayBuffer=====${i.byteLength}")
        }
        //
        //        println(s"i.blob=====${i.blob()}")
        i.blob().toFuture.map{i =>
          try {
            println(i)
            println(i.size)
            val url = file.readAsDataURL(i)
            //            val array = file.readAsArrayBuffer(i)
            //            val text = file.readAsText(i)
            println(s"i.blob data url =====${url}")
            //            println(s"i.blob array buffer=====${array}")
            //            println(s"i.blob text =====${text}")
          }catch {
            case e:Exception=>
              println(e.getMessage())
          }
        }
        i
      }.flatMap { r =>
        println(s"msg sent to $url")
        r.text().toFuture
      }.map { rst =>
        rst
      }
    } catch {
      case e: Exception =>
        val errorDetailMsg = Shortcut.errorDetailMsg(e)
        JsFunc.alert(s"sendGet errMsg: $errorDetailMsg")
        throw e
    }
  }


  def getAndParse[T](
                      url: String,
                      withCookie: Boolean = true)(implicit decoder: Decoder[T]): Future[Either[Error, T]] = {
    import io.circe.parser._
    get(url, withCookie).map{s =>
      println(url, s)
      decode[T](s)
    }
  }


  /*  def getParams: Map[String, String] = {
      val paramStr =
        Option(dom.document.getElementById("urlSearch"))
          .map(_.innerHTML).getOrElse(dom.window.location.search)

      val str1 = paramStr.substring(1)
      val pairs = str1.split("&").filter(s => s.length > 0)
      val tmpMap = pairs.map(_.split("=", 2)).filter(_.length == 2)
      tmpMap.map(d => (d(0), d(1))).toMap
    }*/


}

