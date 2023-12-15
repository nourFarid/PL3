import java.sql.{Connection, DriverManager, ResultSet}


//object RoomDAO {
//  // Database connection details
//  val url = "jdbc:mysql://localhost:3306/hoteldb"
//  val driver = "com.mysql.cj.jdbc.Driver"  // Updated driver class name
//  val username = "root"
//  val password = ""
//
//  // Load the database driver
//  Class.forName(driver)
//
//
////book room
//  // Function to book a room
////  def bookRoom(roomId: Int): Unit = {
////    val connection: Connection = DriverManager.getConnection(url, username, password)
////    try {
////      val statement = connection.createStatement()
////      val updateQuery = s"UPDATE Room SET is_available = false WHERE room_id = $roomId"
////      statement.executeUpdate(updateQuery)
////    } finally {
////      connection.close()
////    }
////  }
//
//  // Function to check room availability
//  def checkAvailability(roomId: Int): Boolean = {
//    val connection: Connection = DriverManager.getConnection(url, username, password)
//    try {
//      val statement = connection.createStatement()
//      val query = s"SELECT is_available FROM Room WHERE room_id = $roomId"
//      val resultSet = statement.executeQuery(query)
//      resultSet.next()
//      resultSet.getBoolean("is_available")
//    } finally {
//      connection.close()
//    }
//  }
//
//
//  // Function to check out a room
//  def checkOutRoom(roomId: Int): Unit = {
//    val connection: Connection = DriverManager.getConnection(url, username, password)
//    try {
//      val updateQuery = "UPDATE Room SET is_available = ? WHERE room_id = ?"
//      val updateQueryDate = "UPDATE Bookings SET check_out_date = ? WHERE room_id = ?"
//
//      val roomUpdateStatement = connection.prepareStatement(updateQuery)
//      val bookingUpdateStatement = connection.prepareStatement(updateQueryDate)
//
//      roomUpdateStatement.setBoolean(1, true)
//      roomUpdateStatement.setInt(2, roomId)
//
//      bookingUpdateStatement.setDate(1, java.sql.Date.valueOf(LocalDate.now()))
//      bookingUpdateStatement.setInt(2, roomId)
//
//      roomUpdateStatement.executeUpdate()
//      bookingUpdateStatement.executeUpdate()
//
//    } finally {
//      connection.close()
//    }
//  }
//
//  // Function to list available rooms
////  def listRoom(): Unit = {
////    val connection: Connection = DriverManager.getConnection(url, username, password)
////
////    try {
////      val statement = connection.createStatement()
////      println("Rooms:")
////
////      val selectQuery = "SELECT * FROM room WHERE is_available=true"
////      val resultSet: ResultSet = statement.executeQuery(selectQuery)
////
////
////
////      while (resultSet.next()) {
////        val roomId = resultSet.getInt("room_number")
////        val roomType = resultSet.getString("room_type")
////        val isAvailable = resultSet.getBoolean("is_available")
////
////        println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable")
////      }
////    } finally {
////      connection.close()
////    }
////  }
////
//
//
//
////  def listRoom(): Unit = {
////    val connection: Connection = DriverManager.getConnection(url, username, password)
////
////    try {
////      val statement = connection.createStatement()
////      println("Rooms:")
////
////      val selectQuery = "SELECT * FROM room WHERE is_available=true"
////      val resultSet: ResultSet = statement.executeQuery(selectQuery)
////
////      if (!resultSet.next()) {
////        // No available rooms
////        println("Full")
////      } else {
////        // Print available rooms
////        do {
////          val roomId = resultSet.getInt("room_number")
////          val roomType = resultSet.getString("room_type")
////          val isAvailable = resultSet.getBoolean("is_available")
////
////          println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable")
////        } while (resultSet.next())
////      }
////    } finally {
////      connection.close()
////    }
////  }
//
//  def listRoom(): String = {
//    val connection: Connection = DriverManager.getConnection(url, username, password)
//
//    try {
//      val statement = connection.createStatement()
//      val selectQuery = "SELECT * FROM room WHERE is_available=true"
//      val resultSet: ResultSet = statement.executeQuery(selectQuery)
//
//      if (!resultSet.next()) {
//        // No available rooms
//        "Full"
//      } else {
//        // Build a string with available room details
//        val roomDetails = new StringBuilder("Rooms:\n")
//
//        do {
//          val roomId = resultSet.getInt("room_number")
//          val roomType = resultSet.getString("room_type")
//          val isAvailable = resultSet.getBoolean("is_available")
//
//          roomDetails.append(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable\n")
//        } while (resultSet.next())
//
//        roomDetails.toString()
//      }
//    }finally {
//      connection.close()
//    }
//  }
//
//
//}
//
//
//
//


import akka.actor.{Actor, Props}

//messages for interacting with RoomDAO actor
//case class CheckAvailability(roomId: Int)
//case class CheckOutRoom(roomId: Int)
case class ListRoom()
class RoomDAO extends Actor {



  override def receive: Receive = {
//    case CheckAvailability(roomId) => sender() ! checkAvailability(roomId)

    case ListRoom() =>listRoom()
  }

  //  // Function to check room availability
// private   def checkAvailability(roomId: Int): Boolean = {
//
//   var connection: Connection = null
//   connection = DatabaseConfig.getConnection
//   val statement = connection.createStatement()
//   if (connection != null) {
//     try {
//
//       val query = s"SELECT is_available FROM Room WHERE room_id = $roomId"
//       val resultSet = statement.executeQuery(query)
//       resultSet.next()
//       resultSet.getBoolean("is_available")
//     } finally {
//       if (connection != null) {
//         DatabaseConfig.closeConnection(connection)
//       }
//     }
//
//   }
// }
    // Function to list available rooms
    def listRoom(): Unit = {
      var connection: Connection = null
      connection = DatabaseConfig.getConnection

      if (connection != null) {


      try {
        val statement = connection.createStatement()
        println("Rooms:")

        val selectQuery = "SELECT * FROM room WHERE is_available=true"
        val resultSet: ResultSet = statement.executeQuery(selectQuery)



        while (resultSet.next()) {
          val roomId = resultSet.getInt("room_number")
          val roomType = resultSet.getString("room_type")
          val isAvailable = resultSet.getBoolean("is_available")

          println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable")
        }
        print("enter room ID: ")
//        if(!resultSet.next()){
//          println("FULL")
//        }
      }catch {

        case e=>println(e.getMessage)
//        case _=>println("ALL ROOMS ARE BOOKED!!!")
      }
      finally {
        connection.close()
      }
    }
}}

object RoomDAO {
  def props: Props = Props[RoomDAO]
}