package org.ncgw.noron

import mhtml.{mount, _}
import org.scalajs.dom
import org.ncgw.noron.utils.PageSwitcher



/**
  * created by wmy on 2019/12/05
  *
  */
object Main extends PageSwitcher {

  private val currentPage = currentHashVar.map { ls =>
    println(s"currentPage change to ${ls.mkString(",")}")

  }


  def show(): Cancelable = {
    switchPageByHash()
    val page =
      <div>
        {currentPage}
      </div>
    mount(dom.document.body, page)
  }


  def main(args: Array[String]): Unit = {}

}
