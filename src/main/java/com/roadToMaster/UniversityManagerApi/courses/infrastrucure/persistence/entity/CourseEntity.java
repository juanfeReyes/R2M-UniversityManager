package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course")
public class CourseEntity {

  @Id
  private String id;

  @Column
  private String name;

  @Column
  private Date startDate;

  @Column
  private Date endDate;

  @Column
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
