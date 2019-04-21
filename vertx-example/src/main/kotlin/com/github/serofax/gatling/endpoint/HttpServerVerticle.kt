package com.github.serofax.gatling.endpoint

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.serofax.gatling.project.api.model.GET_ALL_ACTION
import com.github.serofax.gatling.project.api.model.MESSAGE_ACTION_HEADER
import com.github.serofax.gatling.project.api.model.PROJECT_SERVICE_ADDRESS
import com.github.serofax.gatling.project.api.model.Project
import io.vertx.core.json.Json
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.eventbus.deliveryOptionsOf
import io.vertx.kotlin.core.eventbus.sendAwait
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch


class HttpServerVerticle : CoroutineVerticle() {

  override suspend fun start() {
    val httpServerConfiguration = config.getJsonObject("httpServer")

    Json.mapper.registerModule(KotlinModule())
    val router = createRouter()

    val httpServer = vertx.createHttpServer()
      .requestHandler(router)
      .listenAwait(httpServerConfiguration.getInteger("port"))
    println("Listen on port ${httpServer.actualPort()}")
  }

  private suspend fun getProjects(ctx: RoutingContext) {
    val message = vertx.eventBus()
      .sendAwait<JsonArray>(PROJECT_SERVICE_ADDRESS, JsonObject(), deliveryOptionsOf(headers = mapOf(MESSAGE_ACTION_HEADER to GET_ALL_ACTION)))

    val projects = message.body()
      .map { it as JsonObject }
      .map { it.mapTo(Project::class.java) }

    ctx.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(projects))
  }

  private fun getProjectsNoDb(ctx: RoutingContext) {
    val projects = listOf(Project(name = "Example name",
      shortDescription = "Example short description",
      description = "Example description",
      salesRepresentative = "Example sales representative",
      location = "Example location",
      numberOfPeople = "3",
      duration = "Example duration"))

    ctx.response()
      .putHeader("content-type", "application/json")
      .end(Json.encode(projects))
  }

  private fun createRouter() = Router.router(vertx).apply {
    get("/project").coroutineHandler { ctx -> getProjects(ctx) }
    get("/projectnodb").coroutineHandler { ctx -> getProjectsNoDb(ctx) }
  }


  /**
   * An extension method for simplifying coroutines usage with Vert.x Web routers
   */
  fun Route.coroutineHandler(fn: suspend (RoutingContext) -> Unit): Route = handler { ctx ->
    launch(ctx.vertx().dispatcher()) {
      try {
        fn(ctx)
      } catch (e: Exception) {
        e.printStackTrace()
        ctx.fail(e)
      }
    }
  }
}
