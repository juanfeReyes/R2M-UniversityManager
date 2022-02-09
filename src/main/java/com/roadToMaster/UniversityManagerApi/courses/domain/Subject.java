package com.roadToMaster.UniversityManagerApi.courses.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class Subject {

  @NotNull
  private String id;

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;

  @NotNull
  private Course course;

}
