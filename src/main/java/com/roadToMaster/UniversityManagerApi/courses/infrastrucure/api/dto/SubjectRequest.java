package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@AllArgsConstructor
public class SubjectRequest {

  @NotEmpty
  private String name;

  @NotEmpty
  private String description;

  @NotEmpty
  private String professorUserName;

  @NotEmpty
  private String courseId;

  @Size(min = 1, message = "Should have at least one schedule")
  private List<ScheduleRequest> schedules;
}
