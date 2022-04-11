package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@Table(name = "subject")
@Builder
@Getter
@Setter
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

  @OneToOne
  @JoinColumn(name = "professor")
  private UserEntity professor;
}
