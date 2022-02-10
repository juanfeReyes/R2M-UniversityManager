package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "subject")
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity {

  @Id
  private String id;

  @Column
  private String name;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "course_name",
      foreignKey = @ForeignKey(name = "COURSE_NAME_FK"))
  private CourseEntity course;

  public static SubjectEntity toEntity(Subject subject) {
    return SubjectEntity.builder()
        .id(subject.getId())
        .name(subject.getName())
        .description(subject.getDescription())
        .course(CourseEntity.toEntity(subject.getCourse()))
        .build();
  }
}
