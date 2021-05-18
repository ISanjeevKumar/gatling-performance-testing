import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.commons.stats.assertion._

class LoginSimulation extends Simulation {

  val httpProtocol = http.baseUrl("https://reqres.in/")

  val headers_login = Map("content-type" -> "application/json")

  val scn = scenario("Login with valid data")
    .exec(
      http("Login")
        .post("api/login")
        .headers(headers_login)
        .body(RawFileBody("loginBody.json"))
        .check(status.is(200))
    )

  setUp(scn.inject(atOnceUsers(2)))
    .protocols(httpProtocol)
    .assertions(
      global.successfulRequests.percent.is(100),
      global.responseTime.max.lt(2000),
      global.responseTime.mean.lt(1000),
      global.requestsPerSec.lt(10)
    )
}
