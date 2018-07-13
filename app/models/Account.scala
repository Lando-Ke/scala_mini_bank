package models

import javax.inject.Inject

import anorm._
import javax.inject.Singleton
import play.api.db.Database
import java.time.LocalDate



@Singleton
class Account @Inject()(db: Database) {

  def getBalance(accountNumber: Int) : Int = {
    db withConnection { implicit c =>
      val balance: Int = SQL(
        """
            SELECT balance FROM accounts  WHERE account_number={accountNumber}
          """
      ).on('accountNumber -> accountNumber).as(SqlParser.int("balance").single)
      balance
    }
  }


}