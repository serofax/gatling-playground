package com.github.serofax.gatling.springbootexample.controller;

import com.github.serofax.gatling.springbootexample.model.Project;
import com.github.serofax.gatling.springbootexample.repository.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ProjectController {

    private final ProjectRepository projectRepository;

    @GetMapping(path = "/project", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Project> getAll() {
        return projectRepository.findAll();
    }

    @GetMapping(path = "/projectnodb", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<Project> getAllRaw() {
        return Collections.singletonList(Project.builder()
            .name("Example name")
            .shortDescription("Example short description")
            .description("Example description")
            .salesRepresentative("Example sales representative")
            .location("Example location")
            .numberOfPeople("3")
            .duration("Example duration").build());
    }
}
