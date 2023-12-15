import akka.actor.{Actor, Props}

import java.sql.{Connection, ResultSet}

case class Report()
class ReportDAO extends Actor {

  override def receive: Receive = {
    //    case CheckAvailability(roomId) => sender() ! checkAvailability(roomId)
    case Report() =>report()
  }

  private def report(): Unit = {
    var connection: Connection = null
    connection = DatabaseConfig.getConnection

    if (connection != null) {


      try {
        val statement = connection.createStatement()
        println("Report :")

        val selectQuery1 = "SELECT * FROM room WHERE is_available=true"
        val resultSet1: ResultSet = statement.executeQuery(selectQuery1)
        println("Available Rooms -------------------------------")

        var availableRoomCount = 0
        var unavailableRoomCount = 0
        var totalRoomCount = 0
        while (resultSet1.next()) {
          val roomId = resultSet1.getInt("room_number")
          val roomType = resultSet1.getString("room_type")
          val isAvailable = resultSet1.getBoolean("is_available")

          println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable")

          if (isAvailable) {
            availableRoomCount +=1
          }
        }
        println(s"Total available rooms: $availableRoomCount")

        val selectQuery2 = "SELECT * FROM room WHERE is_available=false"
        val resultSet2: ResultSet = statement.executeQuery(selectQuery2)

        println("UnAvailable Rooms -------------------------------")


        while (resultSet2.next()) {
          val roomId = resultSet2.getInt("room_number")
          val roomType = resultSet2.getString("room_type")
          val isAvailable = resultSet2.getBoolean("is_available")

          println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable=")

          if(!isAvailable) {
            unavailableRoomCount +=1
          }

        }
        println(s"Total unavailable rooms: $unavailableRoomCount")

        val selectQuery3 = "SELECT * FROM room"
        val resultSet3: ResultSet = statement.executeQuery(selectQuery3)
        println("All Rooms -------------------------------")

        while (resultSet3.next()) {
          val roomId = resultSet3.getInt("room_number")
          val roomType = resultSet3.getString("room_type")
          val isAvailable = resultSet3.getBoolean("is_available")

          println(s"Room ID: $roomId, Room Type: $roomType, Available: $isAvailable")
          totalRoomCount +=1
        }
        println(s"Total rooms: $totalRoomCount"+"\n")

      }catch {

        case e=>println(e.getMessage)
        //        case _=>println("ALL ROOMS ARE BOOKED!!!")
      }
      finally {
        connection.close()
      }
    }
  }}

object ReportDAO {
  def props: Props = Props[ReportDAO]
}