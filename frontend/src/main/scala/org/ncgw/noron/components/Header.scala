package org.ncgw.noron.components

import scala.xml.Node

object Header {

  val header =
    <div class="header-container">
      <div class="header-logo">
        <img src="logo.png"></img>
        <p>NNORON</p>
      </div>
      <div>
        <img class="header-op" src="rank.png"></img>
        <img class="header-op" src="add.png"></img>
      </div>
    </div>

  def app:Node = {
    header
  }

}
