package com.roadToMaster.UniversityManagerApi.courses.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

@Getter
@AllArgsConstructor
public class Course {

  private String id;

  @NotNull
  @NotEmpty(message = "Name cannot be empty")
  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;

  private List<Subject> subjects;
}
