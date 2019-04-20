package com.github.serofax.gatling

import io.vertx.config.ConfigRetriever
import io.vertx.config.ConfigRetrieverOptions
import io.vertx.core.Vertx
import io.vertx.core.VertxOptions
import io.vertx.core.impl.cpu.CpuCoreSensor
import io.vertx.core.json.JsonObject
import io.vertx.kotlin.config.configStoreOptionsOf
import io.vertx.kotlin.config.getConfigAwait
import io.vertx.kotlin.core.closeAwait
import io.vertx.kotlin.core.deployVerticleAwait
import io.vertx.kotlin.core.json.json
import io.vertx.kotlin.core.json.obj
import io.vertx.kotlin.core.vertxOptionsOf

suspend fun main() {
  val initVertx = Vertx.vertx()

  val store = configStoreOptionsOf(
    config = json {
      obj("path" to "configuration.yaml")
    },
    type = "file",
    format = "yaml")
  val config = ConfigRetriever.create(initVertx, ConfigRetrieverOptions().addStore(store)).getConfigAwait()
  val vertxConfig = config.getJsonObject("vertx", JsonObject())
  val vertxOptions = vertxOptionsOf(
    workerPoolSize = vertxConfig.getInteger("workerPoolSize", 20),
    internalBlockingPoolSize = vertxConfig.getInteger("internalBlockingPoolSize", 20),
    eventLoopPoolSize = vertxConfig.getInteger("eventLoopPoolSize", CpuCoreSensor.availableProcessors() * 2)
  )

  initVertx.closeAwait();

  val vertx = Vertx.vertx(vertxOptions)
  try {
    vertx.deployVerticleAwait(MainVerticle())
    startMonitor(vertxOptions)
    println("Application started")
  } catch (exception: Throwable) {
    println("Could not start application")
    exception.printStackTrace()
  }
}

fun startMonitor(vertxOptions: VertxOptions) {
  Thread(Runnable {
    while (true) {
      val threadSet = Thread.getAllStackTraces().keys

      val vertxInternalBlocking = threadSet.filter { t -> t.name.contains("vert.x-internal-blocking") }
        .count()
      val vertxInternalWorker = threadSet.filter { t -> t.name.contains("vert.x-worker-thread") }
        .count()
      val vertxEventloop = threadSet.filter { t -> t.name.contains("vert.x-eventloop-thread") }
        .count()

      println("Working: $vertxInternalWorker of max ${vertxOptions.workerPoolSize} | Blocking $vertxInternalBlocking of max ${vertxOptions.internalBlockingPoolSize} | Eventloop $vertxEventloop of max ${vertxOptions.eventLoopPoolSize}  (${threadSet.size} threads in total)")
      Thread.sleep(1000)
    }
  }).start()
}
