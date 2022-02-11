package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Getter
@Setter
public class CourseRequest {

  private String id;

  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;

  public static Course toDomain(CourseRequest courseRequestDTO) {
    return new Course(
        courseRequestDTO.getId(),
        courseRequestDTO.getName(),
        courseRequestDTO.getStartDate(),
        courseRequestDTO.getEndDate(),
        courseRequestDTO.isActive(),
        List.of());
  }
}
