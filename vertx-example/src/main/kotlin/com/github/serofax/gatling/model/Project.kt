package com.github.serofax.gatling.model

data class Project(
  val id: Int? = null,
  val name: String,
  val shortDescription: String,
  val description: String,
  val salesRepresentative: String,
  val location: String,
  val numberOfPeople: String,
  val duration: String
)

