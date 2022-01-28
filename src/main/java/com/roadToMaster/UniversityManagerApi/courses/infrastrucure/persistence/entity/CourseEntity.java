package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Builder
public class CourseEntity {

  private String id;

  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;


  public static CourseEntity toEntity(Course course){
    return CourseEntity.builder()
        .id(course.getId())
        .name(course.getName())
        .startDate(course.getStartDate())
        .endDate(course.getEndDate())
        .active(course.isActive())
        .build();
  }
}
