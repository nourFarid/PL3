import akka.actor.{ ActorSystem}
import scala.io.StdIn.{readInt, readLine}
import scala.util.Random
import scala.util.control.Breaks.{break, breakable}
object HotelApp {
  def main(args: Array[String]): Unit = {

    implicit val system: ActorSystem = ActorSystem("HotelActorSystem")

    val customersActor = system.actorOf(CustomersDAO.props, "customersActor")
    val roomActor = system.actorOf(RoomDAO.props, "roomActor")
    val billingActor= system.actorOf(BillingDAO.props, "billingActor")
    val reportActor= system.actorOf(ReportDAO.props, "reportActor")

    var flag: Int = 1
    var roomId: Int = 1

    println("WELCOME IN OUR HOTEL, PLEASE ENTER WHAT YOU WANT TO DO: ")

    breakable {
      while (flag == 1) {
        println("\nWHAT DO YOU WANT TO DO?\n1 To Book a room \n" +
          "2 To make a bill and check out \n"+"3 To make a report\n" + "0 To EXIT\n")

        var operation = readInt()

        operation match {
          case 1 =>

             roomActor ! ListRoom()
//              println(rooms)
//              print("enter room ID: ")
              roomId = readInt()

                println("enter name, email and phone number: ")
                val name = readLine()
                val email = readLine()
                val phone_number = readLine()
                try {
                  // Use the customersActor to send booking messages
                  customersActor ! BookRoom(roomId, name, email, phone_number)

                  // Use the roomActor to send CheckOutRoom message
                  println("Room booked successfully.")
                } catch {
                  case e =>
                    println(e.getMessage)
                }


          case 2 =>
          //MAKE BILL AND CHECK OUT
            billingActor ! ListBookings()
            print("enter the Booking_id to make a Bill: ")
            val booking_id = readInt()
            print("enter room id to check out: ")
            roomId = readInt()
//            val billValue: Int = Math.abs(Random.nextInt())
            billingActor ! MakeBill(booking_id,roomId)
//            println(s"Bill was made successfully, and it's: $billValue" )
            println("CHECKED OUT!")
            Thread.sleep(1000)





          case 3 => // make report
            //MAKE BILL AND CHECK OUT
            reportActor  ! Report()
            Thread.sleep(1000)
            billingActor ! ListBookings()
            Thread.sleep(1000)

          case 0 => break() // This will break out of the while loop
          case _ => println("invalid operation")
        }
      }
    }

//    // Add a delay before terminating the ActorSystem
//    Thread.sleep(1000) // 1 second delay

    // Terminate the ActorSystem outside the loop
    system.terminate()
//
//    // Wait for the termination to complete before exiting
//    Await.result(system.whenTerminated, Duration.Inf)
  }
}