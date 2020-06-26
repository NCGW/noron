package org.ncgw.noron.utils

import slick.codegen.SourceCodeGenerator
import slick.driver.{JdbcProfile, MySQLDriver}
import slick.jdbc.{JdbcProfile,MySQLProfile}

import scala.concurrent.Await
import scala.concurrent.duration.Duration

/**
 * User: Taoz
 * Date: 7/15/2015
 * Time: 9:33 AM
 */
object MySlickCodeGenerator {



  import concurrent.ExecutionContext.Implicits.global

  val slickProfile = "slick.jdbc.H2Profile"
  val jdbcDriver = "org.h2.Driver"
//  val url = "jdbc:h2:file:./data/VideoMeeting"
  val url = "jdbc:h2:file:/Users/gaohan/Downloads/work/noron/data/noron" //本地测试时尽量修改为本机h2文件地址
  val outputFolder = "target/gencode/genTablesPsql"
  val pkg = "org.ncgw.noron.models"
  val user = "noron"
  val password = "1qaz@WSX"


  //val dbDriver = MySQLDriver

  def genCustomTables() = {

    // fetch data model
    val driver: JdbcProfile =
      Class.forName(slickProfile + "$").getField("MODULE$").get(null).asInstanceOf[JdbcProfile]
    val dbFactory = driver.api.Database
    val db = dbFactory.forURL(url, driver = jdbcDriver,
      user = user, password = password, keepAliveConnection = true)


    // fetch data model
    val modelAction = MySQLProfile.createModel(Some(MySQLProfile.defaultTables)) // you can filter specific tables here
    val modelFuture = db.run(modelAction)

    // customize code generator
    val codeGenFuture = modelFuture.map(model => new SourceCodeGenerator(model) {
      // override mapped table and class name
      override def entityName =
        dbTableName => "r" + dbTableName.toCamelCase

      override def tableName =
        dbTableName => "t" + dbTableName.toCamelCase

      // add some custom import
      // override def code = "import foo.{MyCustomType,MyCustomTypeMapper}" + "\n" + super.code

      // override table generator
      /*    override def Table = new Table(_){
            // disable entity class generation and mapping
            override def EntityType = new EntityType{
              override def classEnabled = false
            }

            // override contained column generator
            override def Column = new Column(_){
              // use the data model member of this column to change the Scala type,
              // e.g. to a custom enum or anything else
              override def rawType =
                if(model.name == "SOME_SPECIAL_COLUMN_NAME") "MyCustomType" else super.rawType
            }
          }*/
    })

    val codeGenerator = Await.result(codeGenFuture, Duration.Inf)
    codeGenerator.writeToFile(
      slickProfile, outputFolder, pkg, "SlickTables", "SlickTables.scala"
    )


  }


  def genDefaultTables() = {

    slick.codegen.SourceCodeGenerator.main(
      Array(slickProfile, jdbcDriver, url, outputFolder, pkg, user, password)
    )


  }


  def main(args: Array[String]) {
    //genDefaultTables()
    genCustomTables()

    println(s"Tables.scala generated in $outputFolder")

  }


}


