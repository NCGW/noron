package org.ncgw.noron.models

// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object SlickTables extends {
  val profile = slick.jdbc.PostgresProfile
} with SlickTables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait SlickTables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = tTask.schema ++ tUser.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table tTask
   *  @param taskId Database column TASK_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param userId Database column USER_ID SqlType(BIGINT)
   *  @param taskContent Database column TASK_CONTENT SqlType(VARCHAR), Length(512,true), Default(None)
   *  @param taskImg Database column TASK_IMG SqlType(VARCHAR), Length(100,true), Default(None)
   *  @param startTime Database column START_TIME SqlType(BIGINT), Default(None)
   *  @param endTime Database column END_TIME SqlType(BIGINT), Default(None)
   *  @param taskType Database column TASK_TYPE SqlType(INTEGER), Default(0)
   *  @param taskProgress Database column TASK_PROGRESS SqlType(INTEGER), Default(0)
   *  @param priority Database column PRIORITY SqlType(INTEGER), Default(0) */
  case class rTask(taskId: Long, userId: Long, taskContent: Option[String] = None, taskImg: Option[String] = None, startTime: Option[Long] = None, endTime: Option[Long] = None, taskType: Int = 0, taskProgress: Int = 0, priority: Int = 0)
  /** GetResult implicit for fetching rTask objects using plain SQL queries */
  implicit def GetResultrTask(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Long]], e3: GR[Int]): GR[rTask] = GR{
    prs => import prs._
    rTask.tupled((<<[Long], <<[Long], <<?[String], <<?[String], <<?[Long], <<?[Long], <<[Int], <<[Int], <<[Int]))
  }
  /** Table description of table TASK. Objects of this class serve as prototypes for rows in queries. */
  class tTask(_tableTag: Tag) extends profile.api.Table[rTask](_tableTag, Some("PUBLIC"), "TASK") {
    def * = (taskId, userId, taskContent, taskImg, startTime, endTime, taskType, taskProgress, priority) <> (rTask.tupled, rTask.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(taskId), Rep.Some(userId), taskContent, taskImg, startTime, endTime, Rep.Some(taskType), Rep.Some(taskProgress), Rep.Some(priority))).shaped.<>({r=>import r._; _1.map(_=> rTask.tupled((_1.get, _2.get, _3, _4, _5, _6, _7.get, _8.get, _9.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column TASK_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val taskId: Rep[Long] = column[Long]("TASK_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_ID SqlType(BIGINT) */
    val userId: Rep[Long] = column[Long]("USER_ID")
    /** Database column TASK_CONTENT SqlType(VARCHAR), Length(512,true), Default(None) */
    val taskContent: Rep[Option[String]] = column[Option[String]]("TASK_CONTENT", O.Length(512,varying=true), O.Default(None))
    /** Database column TASK_IMG SqlType(VARCHAR), Length(100,true), Default(None) */
    val taskImg: Rep[Option[String]] = column[Option[String]]("TASK_IMG", O.Length(100,varying=true), O.Default(None))
    /** Database column START_TIME SqlType(BIGINT), Default(None) */
    val startTime: Rep[Option[Long]] = column[Option[Long]]("START_TIME", O.Default(None))
    /** Database column END_TIME SqlType(BIGINT), Default(None) */
    val endTime: Rep[Option[Long]] = column[Option[Long]]("END_TIME", O.Default(None))
    /** Database column TASK_TYPE SqlType(INTEGER), Default(0) */
    val taskType: Rep[Int] = column[Int]("TASK_TYPE", O.Default(0))
    /** Database column TASK_PROGRESS SqlType(INTEGER), Default(0) */
    val taskProgress: Rep[Int] = column[Int]("TASK_PROGRESS", O.Default(0))
    /** Database column PRIORITY SqlType(INTEGER), Default(0) */
    val priority: Rep[Int] = column[Int]("PRIORITY", O.Default(0))

    /** Foreign key referencing tUser (database name TASK_USER_USER_ID_FK) */
    lazy val tUserFk = foreignKey("TASK_USER_USER_ID_FK", userId, tUser)(r => r.userId, onUpdate=ForeignKeyAction.Restrict, onDelete=ForeignKeyAction.Restrict)
  }
  /** Collection-like TableQuery object for table tTask */
  lazy val tTask = new TableQuery(tag => new tTask(tag))

  /** Entity class storing rows of table tUser
   *  @param userId Database column USER_ID SqlType(BIGINT), AutoInc, PrimaryKey
   *  @param userName Database column USER_NAME SqlType(VARCHAR), Length(64,true), Default(None)
   *  @param coins Database column COINS SqlType(INTEGER), Default(None) */
  case class rUser(userId: Long, userName: Option[String] = None, coins: Option[Int] = None)
  /** GetResult implicit for fetching rUser objects using plain SQL queries */
  implicit def GetResultrUser(implicit e0: GR[Long], e1: GR[Option[String]], e2: GR[Option[Int]]): GR[rUser] = GR{
    prs => import prs._
    rUser.tupled((<<[Long], <<?[String], <<?[Int]))
  }
  /** Table description of table USER. Objects of this class serve as prototypes for rows in queries. */
  class tUser(_tableTag: Tag) extends profile.api.Table[rUser](_tableTag, Some("PUBLIC"), "USER") {
    def * = (userId, userName, coins) <> (rUser.tupled, rUser.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = ((Rep.Some(userId), userName, coins)).shaped.<>({r=>import r._; _1.map(_=> rUser.tupled((_1.get, _2, _3)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column USER_ID SqlType(BIGINT), AutoInc, PrimaryKey */
    val userId: Rep[Long] = column[Long]("USER_ID", O.AutoInc, O.PrimaryKey)
    /** Database column USER_NAME SqlType(VARCHAR), Length(64,true), Default(None) */
    val userName: Rep[Option[String]] = column[Option[String]]("USER_NAME", O.Length(64,varying=true), O.Default(None))
    /** Database column COINS SqlType(INTEGER), Default(None) */
    val coins: Rep[Option[Int]] = column[Option[Int]]("COINS", O.Default(None))
  }
  /** Collection-like TableQuery object for table tUser */
  lazy val tUser = new TableQuery(tag => new tUser(tag))
}
