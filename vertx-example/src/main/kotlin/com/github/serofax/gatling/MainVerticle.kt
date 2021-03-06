package com.github.serofax.gatling

import com.github.serofax.gatling.endpoint.HttpServerVerticle
import com.github.serofax.gatling.project.ProjectVerticle
import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.deploymentOptionsOf
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.coroutines.CoroutineVerticle

class MainVerticle : CoroutineVerticle() {

  override suspend fun start() {
    val store = configStoreOptionsOf(
      config = json {
        obj("path" to "configuration.yaml")
      },
      type = "file",
      format = "yaml")
    val config = ConfigRetriever.create(vertx, ConfigRetrieverOptions().addStore(store)).getConfigAwait()

    println(config)

    vertx.deployVerticleAwait(ProjectVerticle(), deploymentOptionsOf(config = config))
    vertx.deployVerticleAwait(HttpServerVerticle(), deploymentOptionsOf(config = config))
  }
}
