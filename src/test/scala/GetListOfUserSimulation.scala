import scala.concurrent.duration._

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import io.gatling.jdbc.Predef._
import io.gatling.commons.stats.assertion._

class GetListOfUserSimulation extends Simulation {
  val httpProtocol = http.baseUrl("https://reqres.in/")

  val scn = scenario("Get list of users")
    .exec(
      http("ListOfUser")
        .get("api/users?page=2")
        .check(status.is(200))
    )

  setUp(scn.inject(atOnceUsers(5)))
    .protocols(httpProtocol)
    .assertions(
      global.successfulRequests.percent.is(100),
      global.responseTime.max.lt(8000),
      global.responseTime.mean.lt(9850),
      global.requestsPerSec.lt(10)
    )
}
