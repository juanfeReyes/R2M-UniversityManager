package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Getter
@Setter
public class CourseRequest {

  @NotEmpty(message = "name cannot be empty")
  @NotNull
  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;
}
