package com.github.serofax.gatling

import io.vertx.core.Vertx
import io.vertx.kotlin.core.deployVerticleAwait

suspend fun main() {
//  val vertxOptions = vertxOptionsOf(workerPoolSize = 40, internalBlockingPoolSize = 40, eventLoopPoolSize = 20)
//  val vertx = Vertx.vertx(vertxOptions)
  val vertx = Vertx.vertx()
  try {
    vertx.deployVerticleAwait(MainVerticle())
    println("Application started")
  } catch (exception: Throwable) {
    println("Could not start application")
    exception.printStackTrace()
  }
}
