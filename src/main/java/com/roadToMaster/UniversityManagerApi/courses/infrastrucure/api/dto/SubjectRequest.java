package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public class SubjectRequest {

  @NotEmpty
  @NotNull
  private String name;

  @NotEmpty
  @NotNull
  private String description;

  @NotEmpty
  @NotNull
  private String professorUserName;

  @NotEmpty
  @NotNull
  private String courseId;

  @Size(min = 1, message = "Should have at least one schedule")
  @Valid
  private List<ScheduleRequest> schedules;
}
