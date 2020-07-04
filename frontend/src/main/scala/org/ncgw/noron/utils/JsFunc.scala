package org.ncgw.noron.utils

import org.scalajs.dom.{Element, File}
import org.scalajs.dom.html.Image
import org.scalajs.dom.raw.HTMLCanvasElement

import scala.scalajs.js
import scala.scalajs.js.annotation._

/**
  * User: Taoz
  * Date: 11/30/2016
  * Time: 10:24 PM
  */
@js.native
@JSGlobalScope
object JsFunc extends js.Object{

  def decodeURI(str: String): String = js.native

  def decodeURIComponent(str: String): String = js.native

  def unecape(str: String): String = js.native

  def alert(msg: String): Unit = js.native

  def prompt(str: String, placehold: String) : String = js.native

  def fullScreen(element:Element):Unit = js.native

  def exitFullScreen():js.Object = js.native

  def base64ToBlob(urlData:String, `type`:String):js.Object = js.native

  def html2ImgFunc():Unit = js.native

  def rtnBase64(sourceId:String, targetId:String):Unit = js.native

  def drawCanvas():Unit = js.native

  def genBarCode(str:String):Unit = js.native

  def fixOrientation(imgFile:File, imgDes:Image):js.Object = js.native

  def progressBar(str: String, str1: String):Unit = js.native

  def startRecording():Unit = js.native

}
