

import java.sql.{Connection, ResultSet}
import java.time.LocalDate
import java.time.temporal.ChronoUnit

//object BillingDAO {
//  //Database connection details
//  val url = "jdbc:mysql://localhost:3306/HotelDB"
//  val driver = "com.mysql.cj.jdbc.Driver"  // Updated driver class name
//  val username = "root"
//  val password = ""
//
//  // Load the database driver
//  Class.forName(driver)
//
//
//  def makeBill(booking_id:Int): Unit = {
//    val connection: Connection = DriverManager.getConnection(url, username, password)
//    try {   val statement = connection.createStatement()
//      // Generate a positive random integer
//      val billValue: Int = Math.abs(Random.nextInt())
//
//
//      val bookingQuery = "INSERT INTO Billing (booking_id, total_amount) VALUES (?, ?)"
//      val bookingStatement = connection.prepareStatement(bookingQuery)
//      bookingStatement.setInt(1, booking_id)
//      bookingStatement.setInt(2, billValue)
//      bookingStatement.executeUpdate()
//      println(s"bill was made successfully and it's: $billValue")
//    } finally {
//      connection.close()
//    }
//  }
//
//def listBookings():Unit={
//  val connection: Connection = DriverManager.getConnection(url, username, password)
//
//  try {
//    val statement = connection.createStatement()
//    println("Bookings:")
//
//          val selectQuery = "SELECT * FROM bookings"
//          val resultSet: ResultSet = statement.executeQuery(selectQuery)
//
//          while (resultSet.next()) {
//            val booking_id = resultSet.getInt("booking_id")
//            val room_id = resultSet.getInt("room_id")
//            val check_in_date = resultSet.getDate("check_in_date")
//            println(s"booking_id: $booking_id, room_id: $room_id, check_in_date: $check_in_date")
//
//          }
//
//    }
//  finally {
//    connection.close()
//  }
//}
//
//}


import akka.actor.{Actor, Props}
import java.sql.{PreparedStatement}

// Define messages for interacting with BillingDAO actor
case class MakeBill(booking_id: Int,roomId:Int )
case class ListBookings()

class BillingDAO extends Actor {

  override def receive: Receive = {
    case MakeBill(booking_id,roomId) => makeBill(booking_id, roomId: Int)
    case ListBookings() => listBookings()
  }

  private def makeBill(booking_id: Int,roomId: Int): Unit = {

    var connection: Connection = null
    var check_in_date :LocalDate=null
    val check_out_date=LocalDate.now()
    var dayValue= 100
    connection = DatabaseConfig.getConnection
    val statement = connection.createStatement()
    if (connection != null) {
    try {
      val testQuery=s"select check_in_date from Bookings where booking_id= $booking_id"
      val resultSet: ResultSet = statement.executeQuery(testQuery)
      if(resultSet.next()){
        check_in_date = resultSet.getDate("check_in_date").toLocalDate()
      }

//      println("check in date:" + check_in_date)


      val daysBetween: Long = ChronoUnit.DAYS.between(check_in_date, check_out_date)
//println("DAYS: " + daysBetween)

      var billValue = dayValue * daysBetween
//      println("BILLLLLLLL:"+billValue)
      val bookingQuery = "INSERT INTO Billing (booking_id, total_amount) VALUES (?, ?)"
      val bookingStatement: PreparedStatement = connection.prepareStatement(bookingQuery)
      bookingStatement.setInt(1, booking_id)
      bookingStatement.setLong(2, billValue)
      bookingStatement.executeUpdate()
      val updateQuery = "UPDATE Room SET is_available = ? WHERE room_id = ?"
      val updateQueryDate = "UPDATE Bookings SET check_out_date = ? WHERE room_id = ?"

      val roomUpdateStatement = connection.prepareStatement(updateQuery)
      val bookingUpdateStatement = connection.prepareStatement(updateQueryDate)

      roomUpdateStatement.setBoolean(1, true)
      roomUpdateStatement.setInt(2, roomId)

      bookingUpdateStatement.setDate(1, java.sql.Date.valueOf(check_out_date))
      bookingUpdateStatement.setInt(2, roomId)

      roomUpdateStatement.executeUpdate()
      bookingUpdateStatement.executeUpdate()
      println(s"Bill was made successfully, and it's: $billValue" )
//      println("CHECKED OUT!")


    }catch {
      case e: Throwable => println(e.getMessage())
    }
    finally {

      if (connection != null) {
        DatabaseConfig.closeConnection(connection)
      }
    }
  }}









  private def listBookings(): Unit = {
    var connection: Connection = null
    connection = DatabaseConfig.getConnection
    val statement = connection.createStatement()
    if (connection != null) {
    try {

      println("Bookings:")

      val selectQuery = "SELECT * FROM bookings where check_out_date is null"
      val resultSet: ResultSet = statement.executeQuery(selectQuery)

      while (resultSet.next()) {
        val booking_id = resultSet.getInt("booking_id")
        val room_id = resultSet.getInt("room_id")
        val check_in_date = resultSet.getDate("check_in_date")
        println(s"booking_id: $booking_id, room_id: $room_id, check_in_date: $check_in_date")
      }
    }catch {
      case e: Throwable =>println(e.getMessage())
    }
    finally {
      if (connection != null) {
        DatabaseConfig.closeConnection(connection)
      }
    }
  }
}}

object BillingDAO {
  def props: Props = Props[BillingDAO]
}
