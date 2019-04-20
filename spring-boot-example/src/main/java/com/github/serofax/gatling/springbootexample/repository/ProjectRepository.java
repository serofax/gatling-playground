package com.github.serofax.gatling.springbootexample.repository;

import com.github.serofax.gatling.springbootexample.model.Project;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProjectRepository extends JpaRepository<Project, Integer> {
}
