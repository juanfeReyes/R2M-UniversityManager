package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.Getter;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
public class SubjectCreationRequest extends SubjectRequest{
  @NotEmpty
  @NotNull
  private String courseId;

  public SubjectCreationRequest(@NotEmpty @NotNull String name,
                                @NotEmpty @NotNull String description,
                                @NotEmpty @NotNull String professorUserName,
                                @NotEmpty @NotNull String courseId,
                                @Size(min = 1, message = "Should have at least one schedule") @Valid List<ScheduleRequest> schedules) {
    super(name, description, professorUserName, schedules);
    this.courseId = courseId;
  }
}
