package controllers

import javax.inject.{Inject, Singleton}
import models.Transaction
import java.time.LocalDate
import play.api.libs.json._
import play.api._
import play.api.mvc._

case class postData(amount: Double)


@Singleton
class AccountController @Inject()(cc: ControllerComponents, Transaction: Transaction) extends AbstractController(cc) {

  val dailyWithdrawalCountLimit = 3
  val dailyDepositCountLimit = 4
  val maxWithdrawalLimit = 20000
  val dailyWithdrawalLimit = 50000
  val maxDepositPerTransaction = 40000
  val dailyDepositLimit = 150000



  def getBalance(account_number: Int) = Action {
    val balance = Transaction.getAccountBalance(account_number)
    val msg = "Account balance retrieved successfully."
    val response: JsValue = Json.obj(
      "success" -> true,
      "message" -> msg,
      "balance" -> balance
    )
    Ok(response)
  }


  implicit val transReads = (__ \ "amount").read[Double].map(resource => postData(resource))

  def deposit(account_number: Int) = Action(parse.json) { request => {
    unMarshalJsValue(request) { resource: postData =>
      val transaction = postData(resource.amount)
      if (transaction.amount > maxDepositPerTransaction) {

        val msg = "You cannot deposit more than  KES " + maxDepositPerTransaction.toString() + " at once"
        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg ,
        )

        Forbidden(response)

      } else if (Transaction.getTransactionCountByDate("deposit", LocalDate.now(), account_number) >= dailyDepositCountLimit) {
        val msg = "You have reached your maximum number of (" + dailyDepositCountLimit.toString() + ") deposits today"
        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        Forbidden(response)

      } else if (Transaction.getTotalTransactionsByDate("deposit", LocalDate.now(), account_number) >= dailyDepositLimit) {
        val msg = "You cannot deposit more than  KES " + dailyDepositLimit.toString() + " in a day"

        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        Forbidden(response)

      } else {
        val currentBalance = Transaction.getAccountBalance(account_number)
        Transaction.saveTransaction(transaction.amount, "deposit", account_number, currentBalance)
        val newBalance = Transaction.getAccountBalance(account_number)
        val msg = "Deposit successful."
        val response: JsValue = Json.obj(
          "success" -> true,
          "message" -> msg,
          "transaction_type" -> "deposit",
          "amount" -> transaction.amount,
          "balance" -> newBalance
        )
        Ok(response)

      }
    }
  }

  }


  def withdraw(account_number: Int) = Action(parse.json) { request => {
    unMarshalJsValue(request) { resource: postData =>
      val transaction = postData(resource.amount)
      val currentBalance = Transaction.getAccountBalance(account_number)
      if (transaction.amount > maxWithdrawalLimit) {
        Forbidden(Json.obj("msg" -> "Exceeded Maximum Withdrawal Per Transaction"))
        val msg = "You cannot withdraw more than  KES " + maxWithdrawalLimit.toString() + " at once"

        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        Forbidden(response)
      }
      else if (Transaction.getTransactionCountByDate("withdrawal", LocalDate.now(), account_number) >= dailyWithdrawalCountLimit) {
        val msg = "You have reached your maximum number of withdrawals for today"

        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        TooManyRequests(response)

      }
      else if (Transaction.getTotalTransactionsByDate("withdrawal", LocalDate.now(), account_number) + transaction.amount >= dailyWithdrawalLimit) {
        Forbidden(Json.obj("msg" -> "Exceeded Maximum  Withdrawal for Today"))
        val msg = "You cannot withdraw more than  KES " + dailyWithdrawalLimit.toString() + " in a day"

        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        Forbidden(response)
      } else if (transaction.amount > currentBalance) {
        val msg = "Withdrawal exceeds current available balance"

        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> msg
        )

        Forbidden(response)
      }
      else {
        Transaction.saveTransaction(transaction.amount, "withdrawal", account_number, currentBalance)
        val newBalance = Transaction.getAccountBalance(account_number)

        val msg = "Withdrawal successful"

        val response: JsValue = Json.obj(
          "success" -> true,
          "message" -> msg,
          "transaction_type" -> "withdrawal",
          "amount" -> transaction.amount,
          "balance" -> newBalance
        )

        Ok(response)

      }
    }
  }

  }


  def unMarshalJsValue[R](request: Request[JsValue])(block: R => Result)(implicit rds: Reads[R]): Result =
    request.body.validate[R](rds).fold(
      valid = block,
      invalid = e => {
        val error = e.mkString
        Logger.error(error)
        val response: JsValue = Json.obj(
          "success" -> false,
          "message" -> "Please pass a valid amount",
        )

        BadRequest(response)
      }
    )

}
