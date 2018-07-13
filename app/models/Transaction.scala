package models

import java.time.LocalDate

import anorm._
import javax.inject.{Inject, Singleton}
import play.api.db.Database



@Singleton
class Transaction @Inject()(db: Database) {


  def getTransactionCountByDate(transactionType: String, transactionDate: LocalDate, accountNumber: Int): Int = {
    db.withConnection { implicit c =>
      val result: Int = SQL(
        """
            SELECT COALESCE(count(*),0) as total_count FROM transactions  WHERE type={transactionType}
            AND account_number ={accountNumber}
            AND created_at={transactionDate}
          """
      ).on('transactionType -> transactionType, 'transactionDate -> transactionDate.toString, 'accountNumber ->accountNumber).as(SqlParser.int("total_count").single)
      result
    }
  }

  def getAccountBalance(accountNumber: Int) : Double = {
    db withConnection { implicit c =>
      val balance: Double = SQL(
        """
            SELECT balance FROM accounts  WHERE account_number={accountNumber}
          """
      ).on('accountNumber -> accountNumber).as(SqlParser.double("balance").single)
      balance
    }
  }


  def getTotalTransactionsByDate(transactionType: String, transactionDate: LocalDate, accountNumber: Int): Double ={
    db.withConnection { implicit c =>
      val result: Double = SQL(
        """
            SELECT COALESCE(sum(amount), 0) as total FROM transactions WHERE type={transactionType}
            AND account_number = {accountNumber}
            AND created_at={transactionDate}
          """
      ).on('transactionType -> transactionType, 'transactionDate -> transactionDate.toString, 'accountNumber ->accountNumber).as(SqlParser.double("total").single)
      result
    }
  }

  def saveTransaction(amount: Double, transactionType: String, accountNumber: Int, balance: Double): Long = {
    db.withConnection { implicit c =>
      val id: Option[Long] = SQL(
        """
           insert into transactions (account_number, amount, type) values ({accountNumber}, {amount}, {transactionType})
          """
      ).on('accountNumber ->accountNumber,'amount -> amount, 'transactionType -> transactionType).executeInsert()

      val newBalance = transactionType match {
        case "deposit"  => balance + amount
        case "withdrawal" => balance - amount
        case _          => balance
      }
      this.updateBalance(newBalance, accountNumber)

      val ID = id match {
        case Some(id) => id
        case None => 0
      }
      ID
    }
  }

  def updateBalance(balance: Double, account: Int): Long = {
    db.withConnection { implicit c =>
      val result: Int = SQL("update accounts SET balance = {balance} WHERE account_number ={account}").
        on('balance -> balance, 'account -> account)
        .executeUpdate()
      result
    }

  }

}