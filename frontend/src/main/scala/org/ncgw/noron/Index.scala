package org.ncgw.noron

import scala.xml.Node

/**
  * Created by wangyinan.
  */
trait Index {
  def app: Node
  def cancel: Unit = ()
  val pageName = this.getClass.getSimpleName
  val url = "#" + pageName

}
