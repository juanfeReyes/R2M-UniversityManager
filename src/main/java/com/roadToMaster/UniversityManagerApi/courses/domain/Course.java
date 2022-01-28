package com.roadToMaster.UniversityManagerApi.courses.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@NoArgsConstructor
public class Course {

  @NotNull
  @NotEmpty(message = "Id cannot be empty")
  private String id;

  @NotNull
  @NotEmpty(message = "Name cannot be empty")
  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;
}
