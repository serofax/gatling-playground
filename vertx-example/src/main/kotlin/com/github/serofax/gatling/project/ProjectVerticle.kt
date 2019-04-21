package com.github.serofax.gatling.project

import com.github.serofax.gatling.project.api.model.GET_ALL_ACTION
import com.github.serofax.gatling.project.api.model.MESSAGE_ACTION_HEADER
import com.github.serofax.gatling.project.api.model.PROJECT_SERVICE_ADDRESS
import com.github.serofax.gatling.project.repository.ProjectRepository
import io.vertx.core.Vertx
import io.vertx.core.eventbus.EventBus
import io.vertx.core.eventbus.Message
import io.vertx.core.eventbus.MessageConsumer
import io.vertx.core.json.JsonArray
import io.vertx.core.json.JsonObject
import io.vertx.ext.asyncsql.PostgreSQLClient
import io.vertx.kotlin.coroutines.CoroutineVerticle
import io.vertx.kotlin.coroutines.dispatcher
import kotlinx.coroutines.launch

class ProjectVerticle : CoroutineVerticle() {

  lateinit var projectService: ProjectRepository

  override suspend fun start() {
    val databaseConfig = config.getJsonObject("database")
    val postgresClient = PostgreSQLClient.createShared(vertx, databaseConfig)
    projectService = ProjectRepository(postgresClient!!)

    vertx.eventBus().coroutineConsumer<JsonObject>(vertx, PROJECT_SERVICE_ADDRESS) { message ->
      when (message.headers().get(MESSAGE_ACTION_HEADER)) {
        GET_ALL_ACTION -> {
          val projects = projectService.getAll()

          val projectsAsJsonArray = projects
            .map { JsonObject.mapFrom(it) }
            .let { JsonArray(it) }

          message.reply(projectsAsJsonArray)
        }
      }
    }
  }


  fun <T> EventBus.coroutineConsumer(vx: Vertx, address: String, fn: suspend (Message<T>) -> Unit): MessageConsumer<T> = consumer<T>(address) { msg ->
    launch(vx.dispatcher()) {
      try {
        fn(msg)
      } catch (e: Throwable) {
        e.printStackTrace()
        msg.fail(500, e.message)
      }
    }
  }
}
