package com.github.serofax.gatling

import scala.concurrent.duration._
import io.gatling.core.Predef._
import io.gatling.http.Predef._

class ProjectPerformanceSimulation extends Simulation {

    val httpProtocol = http
        .baseUrl("http://localhost:8080")
        .acceptHeader("application/json")
        .shareConnections

    val callProjectsApi = scenario("ProjectPerformanceSimulation")
        .exec(http("projects")
            .get("/project"))

    val callProjectsApiWithoutDb = scenario("ProjectPerformanceSimulation With no Database")
        .exec(http("projects")
            .get("/projectnodb"))

    // Scaling test
    //  setUp(callProjectsApi.inject(rampUsers(10000) during (3 minutes)).protocols(httpProtocol))

    // Endurance test vertx can handle it on MBP 13 2016 okayish
    // setUp(callProjectsApi.inject(rampUsersPerSec(1) to 2500 during (2 minutes), constantUsersPerSec(2000) during (40 minutes)).protocols(httpProtocol))

    // Basic scaling test
    setUp(callProjectsApi.inject(rampUsersPerSec(1) to 500 during (30 seconds), constantUsersPerSec(450) during (1 minute))).protocols(httpProtocol)

    // Scaling test without database
    //  setUp(callProjectsApiWithoutDb.inject(rampUsersPerSec(1) to 12000 during (30 seconds), constantUsersPerSec(11000) during (1 minute)).protocols(httpProtocol))
}
