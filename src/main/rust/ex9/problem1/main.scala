//https://scastie.scala-lang.org/

import akka.actor._
import scala.concurrent._
import scala.concurrent.duration._
import ExecutionContext.Implicits.global

import akka.pattern.ask
import akka.util.Timeout

// Nachrichten
case class Deposit(amount: Int)
case class Withdraw(amount: Int)
case object GetBalance

// Account Actor
class AccountActor extends Actor {
  private var balance = 100

  def getBalance = balance

  def receive: Receive = {
    case Deposit(amount) =>
      balance += amount
    case Withdraw(amount) =>
      balance -= amount
    case GetBalance =>
      sender() ! balance
  }
}

// Test
object BankingApp extends App {
  val system = ActorSystem("BankSystem")
  val account = system.actorOf(Props(classOf[AccountActor]), "account")

  val ops = 100
  val amount = 1

  def task(name: String): Future[Unit] = Future {
    for (i <- 1 to ops) {
      account ! Deposit(amount)
      Thread.sleep(1)
      account ! Withdraw(amount)
    }
  }

  val t1 = task("T1")
  val t2 = task("T2")

  val done = for {
    _ <- t1
    _ <- t2
  } yield ()

  done.onComplete { _ =>
    println("=== Both tasks finished ===")
    implicit val timeout: Timeout = Timeout(5.seconds)

    val futureBalance = (account ? GetBalance).mapTo[Int]
    futureBalance.onComplete {
      case scala.util.Success(balance) =>
        println(s"Final balance: $balance (expected: 100)")
        system.terminate()
      case scala.util.Failure(ex) =>
        println(s"Failed to get balance: $ex")
        system.terminate()
    }
  }
}
