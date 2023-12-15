import java.sql.{Connection, SQLException, Statement}
import java.time.LocalDate

//
//object CustomersDAO {
//  // Database connection details
//  val url = "jdbc:mysql://localhost:3306/HotelDB"
//  val driver = "com.mysql.cj.jdbc.Driver" // Updated driver class name
//  val username = "root"
//  val password = ""
//6
//  // Load the database driver
//  Class.forName(driver)
//
//  def bookRoom(roomId: Int, name: String, email: String, phone_number: String): Unit = {
//    val connection: Connection = DriverManager.getConnection(url, username, password)
//
//    try {
//      val statement = connection.createStatement()
//
//      try {
//        // Insert into Customers table
//        val insertQuery = "INSERT INTO Customers (name, email, phone_number) VALUES (?, ?, ?)"
//        val preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
//        preparedStatement.setString(1, name)
//        preparedStatement.setString(2, email)
//        preparedStatement.setString(3, phone_number)
//
//        // Execute the insert statement
//        preparedStatement.executeUpdate()
//      } catch {
//        case e: SQLException =>
//          println(e.getMessage)
//      }
//
//      // Retrieve customer_id after the insert using getGeneratedKeys
//      val getCustomerIdQuery = "SELECT LAST_INSERT_ID() AS customer_id"
//      val resultSet = statement.executeQuery(getCustomerIdQuery)
//      val customerId = if (resultSet.next()) resultSet.getInt("customer_id") else -1
//      println(customerId)
//
//      // Update Room availability
//      val updateQuery = s"UPDATE Room SET is_available = false WHERE room_id = $roomId"
//      statement.executeUpdate(updateQuery)
//
//      // Get the current date
//      val check_in_date = LocalDate.now()
//
//      // Insert into Bookings table
//      val bookingQuery = "INSERT INTO Bookings (room_id, customer_id, check_in_date) VALUES (?, ?, ?)"
//      val bookingStatement = connection.prepareStatement(bookingQuery)
//      bookingStatement.setInt(1, roomId)
//      bookingStatement.setInt(2, customerId)
//      bookingStatement.setDate(3, java.sql.Date.valueOf(check_in_date))
//      bookingStatement.executeUpdate()
//
//    } catch {
//      case e: SQLException =>
//        // Handle any SQL exceptions
//        e.printStackTrace()
//    } finally {
//      connection.close()
//    }
//  }
//}

//import akka.actor.Actor
//import java.sql.{Connection, DriverManager, SQLException, Statement}
//import java.time.LocalDate
//
//class CustomersDAO extends Actor {
//
//  // Database connection details
//  val url = "jdbc:mysql://localhost:3306/HotelDB"
//  val driver = "com.mysql.cj.jdbc.Driver"
//  val username = "root"
//  val password = ""
//
//  // Load the database driver
//  Class.forName(driver)
//
//  override def receive: Receive = {
//    case BookRoom(roomId, name, email, phone_number) =>
//      val connection: Connection = DriverManager.getConnection(url, username, password)
//
//      try {
//        val statement = connection.createStatement()
//
//        try {
//          // Insert into Customers table
//          val insertQuery = "INSERT INTO Customers (name, email, phone_number) VALUES (?, ?, ?)"
//          val preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
//          preparedStatement.setString(1, name)
//          preparedStatement.setString(2, email)
//          preparedStatement.setString(3, phone_number)
//
//          // Execute the insert statement
//          preparedStatement.executeUpdate()
//        } catch {
//          case e: SQLException =>
//            println(e.getMessage)
//        }
//
//        // Retrieve customer_id after the insert using getGeneratedKeys
//        val getCustomerIdQuery = "SELECT LAST_INSERT_ID() AS customer_id"
//        val resultSet = statement.executeQuery(getCustomerIdQuery)
//        val customerId = if (resultSet.next()) resultSet.getInt("customer_id") else -1
//        println(customerId)
//
//        // Update Room availability
//        val updateQuery = s"UPDATE Room SET is_available = false WHERE room_id = $roomId"
//        statement.executeUpdate(updateQuery)
//
//        // Get the current date
//        val check_in_date = LocalDate.now()
//
//        // Insert into Bookings table
//        val bookingQuery = "INSERT INTO Bookings (room_id, customer_id, check_in_date) VALUES (?, ?, ?)"
//        val bookingStatement = connection.prepareStatement(bookingQuery)
//        bookingStatement.setInt(1, roomId)
//        bookingStatement.setInt(2, customerId)
//        bookingStatement.setDate(3, java.sql.Date.valueOf(check_in_date))
//        bookingStatement.executeUpdate()
//
//      } catch {
//        case e: SQLException =>
//          // Handle any SQL exceptions
//          e.printStackTrace()
//      } finally {
//        connection.close()
//      }
//  }
//}
//case class BookRoom(roomId: Int, name: String, email: String, phone_number: String)




import akka.actor.{Actor, Props}


//messages for interacting with CustomersDAO actor
case class BookRoom(roomId: Int, name: String, email: String, phone_number: String)

class CustomersDAO extends Actor {


  override def receive: Receive = {
    case BookRoom(roomId, name, email, phone_number) => bookRoom(roomId, name, email, phone_number)
  }

  private def bookRoom(roomId: Int, name: String, email: String, phone_number: String): Unit = {
    var connection: Connection = null
    try {


      connection = DatabaseConfig.getConnection
      if (connection != null) {
        val statement = connection.createStatement()


        try {
          // Insert into Customers table
          val insertQuery = "INSERT INTO Customers (name, email, phone_number) VALUES (?, ?, ?)"
          val preparedStatement = connection.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS)
          preparedStatement.setString(1, name)
          preparedStatement.setString(2, email)
          preparedStatement.setString(3, phone_number)

          // Execute the insert statement
          preparedStatement.executeUpdate()
        } catch {
          case e: SQLException =>
            println(e.getMessage)
        }

        // Retrieve customer_id after the insert using getGeneratedKeys
        val getCustomerIdQuery = "SELECT LAST_INSERT_ID() AS customer_id"
        val resultSet = statement.executeQuery(getCustomerIdQuery)
        val customerId = if (resultSet.next()) resultSet.getInt("customer_id") else -1
        //      println(customerId)

        // Update Room availability
        val updateQuery = s"UPDATE Room SET is_available = false WHERE room_id = $roomId"
        statement.executeUpdate(updateQuery)

        // Get the current date
        val check_in_date = LocalDate.now()

        // Insert into Bookings table
        val bookingQuery = "INSERT INTO Bookings (room_id, customer_id, check_in_date) VALUES (?, ?, ?)"
        val bookingStatement = connection.prepareStatement(bookingQuery)
        bookingStatement.setInt(1, roomId)
        bookingStatement.setInt(2, customerId)
        bookingStatement.setDate(3, java.sql.Date.valueOf(check_in_date))
        bookingStatement.executeUpdate()
      }
    } catch {
      case e: SQLException =>
        //SQL exceptions
        e.printStackTrace()
    } finally {
      if (connection != null) {
        DatabaseConfig.closeConnection(connection)
      }    }
  }
}

object CustomersDAO {
  def props: Props = Props[CustomersDAO]
}