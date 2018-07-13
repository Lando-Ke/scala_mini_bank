package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._


class AccountControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "AccountController " should {

    "make a get request to the /account/balance endpoint" in {
      val request = FakeRequest(GET, "/account/balance")
      val balance = route(app, request).get

      status(balance) mustBe OK
      contentType(balance) mustBe Some("application/json")
    }
    //Got stuck trying to test post endpoints
//    "make a post request to the /account/deposit endpoint" in {
//      val request = FakeRequest(POST, "/account/deposit").withJsonBody(Json.parse("""{ "amount": "100" }"""))
//
//      val result = route(app, request).get
//
//      status(result) mustBe OK
//    }
  }
  "HomeController " should {

    "make a get request to the / page" in {
      val request = FakeRequest(GET, "/")
      val home = route(app, request).get

      status(home) mustBe OK
    }
  }
}
