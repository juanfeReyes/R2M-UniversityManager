package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class SubjectResponse {

  private String id;

  private String name;

  private String description;

  private String professorId;

  private String courseId;

  private List<Schedule> schedules;
}
