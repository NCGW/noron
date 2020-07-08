package org.ncgw.noron.components

import org.ncgw.noron.common.Constant
import org.scalajs.dom

import scala.xml.Node

object Header {

  def goToInput():Unit = {
    dom.window.location.hash = s"#/input/${Constant.fakeUserId}"
  }

  def goToRank():Unit = {
    dom.window.location.hash = s"#/rank/${Constant.fakeUserId}"
  }

  def goToList():Unit = {
    dom.window.location.hash = s"#/taskList"
  }

  def gotoTimeLine():Unit = {
    dom.window.location.hash = ""

  }

  val header =
    <div class="header-container">
      <div class="header-logo">
        <img src="/noron/static/img/dragon.png"></img>
        <p>NORON</p>
      </div>
      <div class="header-op">
        <img src="/noron/static/img/timeline.png" onclick={() => gotoTimeLine()}></img>
        <img src="/noron/static/img/list.png" onclick={() => goToList()}></img>
        <img src="/noron/static/img/rankfill.png" onclick={() => goToRank()}></img>
        <img src="/noron/static/img/add.png" onclick={() => goToInput()}></img>
      </div>
    </div>

  def app:Node = {
    header
  }

}
