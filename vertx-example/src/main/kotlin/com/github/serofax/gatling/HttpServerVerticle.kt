package com.github.serofax.gatling

import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.github.serofax.gatling.model.Project
import com.github.serofax.gatling.service.ProjectService
import io.vertx.core.json.Json
import io.vertx.ext.web.Route
import io.vertx.ext.web.Router
import io.vertx.ext.web.RoutingContext
import io.vertx.kotlin.core.http.listenAwait
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch


class HttpServerVerticle : CoroutineVerticle() {
  lateinit var projectService: ProjectService

  override suspend fun start() {
    val databaseConfiguration = config.getJsonObject("database")
    val httpServerConfiguration = config.getJsonObject("httpServer")

    Json.mapper.registerModule(KotlinModule())
    val router = createRouter()

    projectService = ProjectService(vertx, databaseConfiguration)

    val httpServer = vertx.createHttpServer()
      .requestHandler(router::accept)
      .listenAwait(httpServerConfiguration.getInteger("port"))
    println("Listen on port ${httpServer.actualPort()}")
  }

  private suspend fun getProjects(ctx: RoutingContext) {
    val projects = projectService.getAll()
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
