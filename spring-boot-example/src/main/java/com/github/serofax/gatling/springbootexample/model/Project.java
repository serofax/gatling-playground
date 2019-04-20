package com.github.serofax.gatling.springbootexample.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Project {
  @Column(name = "id")
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;
  @Column(name = "name")
  private String name;
  @Column(name = "short_description")
  private String shortDescription;
  @Column(name = "description")
  private String description;
  @Column(name = "sales_representative")
  private String salesRepresentative;
  @Column(name = "location")
  private String location;
  @Column(name = "number_of_people")
  private String numberOfPeople;
  @Column(name = "duration")
  private String duration;
}
