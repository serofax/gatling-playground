package com.github.serofax.gatling.project.repository

import com.github.serofax.gatling.project.api.model.Project
import io.vertx.core.json.JsonObject
import io.vertx.ext.asyncsql.AsyncSQLClient
import io.vertx.kotlin.ext.sql.getConnectionAwait
import io.vertx.kotlin.ext.sql.queryAwait

class ProjectRepository(private val postgresClient: AsyncSQLClient) {

  fun mapToProject(row: JsonObject) = Project(
    id = row.getInteger("id"),
    name = row.getString("name"),
    shortDescription = row.getString("short_description"),
    description = row.getString("description"),
    salesRepresentative = row.getString("sales_representative"),
    location = row.getString("location"),
    numberOfPeople = row.getString("number_of_people"),
    duration = row.getString("duration")
  )

  suspend fun getAll(): List<Project> {
    postgresClient.getConnectionAwait().use {
      val resultSet = it.queryAwait("SELECT * FROM project")
      return resultSet.rows.map(this::mapToProject).toList()
    }
  }
}


