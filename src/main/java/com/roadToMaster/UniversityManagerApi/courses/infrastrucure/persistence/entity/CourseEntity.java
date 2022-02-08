package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Builder
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course")
public class CourseEntity {

  @Column
  private String id;

  @Id
  private String name;

  @Column
  private Date startDate;

  @Column
  private Date endDate;

  @Column
  private boolean active;


  public static CourseEntity toEntity(Course course) {
    return CourseEntity.builder()
        .id(course.getId())
        .name(course.getName())
        .startDate(course.getStartDate())
        .endDate(course.getEndDate())
        .active(course.isActive())
        .build();
  }

  public static Course toDomain(CourseEntity courseEntity) {
    return new Course(
        courseEntity.getId(),
        courseEntity.getName(),
        courseEntity.getStartDate(),
        courseEntity.getEndDate(),
        courseEntity.isActive()
    );
  }
}
