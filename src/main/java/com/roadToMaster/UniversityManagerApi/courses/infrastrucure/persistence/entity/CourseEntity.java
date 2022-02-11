package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import lombok.*;

import javax.persistence.*;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Builder
@Getter
@Setter
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

  @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
  private List<SubjectEntity> subjects;

  public static CourseEntity toEntity(Course course) {
    var courseEntity = CourseEntity.builder()
        .id(course.getId())
        .name(course.getName())
        .startDate(course.getStartDate())
        .endDate(course.getEndDate())
        .active(course.isActive())
        .build();
    var subjectEntities = course.getSubjects().stream().map(subject -> SubjectEntity.toEntity(subject, courseEntity))
        .collect(Collectors.toList());
    courseEntity.setSubjects(subjectEntities);
    return courseEntity;
  }

  public static Course toDomain(CourseEntity courseEntity) {
    var subjects = courseEntity.getSubjects().stream()
        .map((entity) -> SubjectEntity.toDomain(entity, null)).collect(Collectors.toList());
    return new Course(
        courseEntity.getId(),
        courseEntity.getName(),
        courseEntity.getStartDate(),
        courseEntity.getEndDate(),
        courseEntity.isActive(),
        subjects
    );
  }
}
