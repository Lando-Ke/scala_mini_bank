package controllers

import org.scalatestplus.play._
import org.scalatestplus.play.guice._
import play.api.test._
import play.api.test.Helpers._
import play.api.libs.json._
import play.api._
import play.api.mvc._
import play.api.mvc.EssentialAction
import akka.stream.Materializer


class AccountControllerSpec extends PlaySpec with GuiceOneAppPerTest with Injecting {

  "AccountController " should {

    "make a get request to the /account/balance endpoint" in {
      val request = FakeRequest(GET, "/account/515276006/balance")
      val balance = route(app, request).get

      status(balance) mustBe OK
      contentType(balance) mustBe Some("application/json")
    }
  }

//    implicit lazy val materializer: Materializer = app.materializer
//
//    "An  action" should {
//      "can parse a JSON body" in {
//        val action: EssentialAction = Action { request =>
//          val value = (request.body.asJson.get \ "amount").as[String]
//          Ok(value)
//        }
//
//
//        val request = FakeRequest(POST, "/account/515276006/deposit").withJsonBody(Json.parse("""{ "amount": "100" }"""))
//
//        val result = call(action, request)
//
//        status(result) mustEqual OK
//
//      }
//    }


}
