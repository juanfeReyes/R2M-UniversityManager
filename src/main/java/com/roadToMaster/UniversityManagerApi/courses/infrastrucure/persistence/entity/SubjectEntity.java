package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import lombok.*;

import javax.persistence.*;
import java.util.List;
import java.util.stream.Collectors;

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

  @OneToOne
  @JoinColumn(name = "professor")
  private UserEntity professor;

  @OneToMany(mappedBy = "subject", cascade = CascadeType.ALL)
  private List<ScheduleEntity> schedules;

  @ManyToOne
  @JoinColumn(name = "course_name",
      foreignKey = @ForeignKey(name = "COURSE_NAME_FK"))
  private CourseEntity course;

  public static SubjectEntity toEntity(Subject subject, CourseEntity courseEntity) {

    var entity = SubjectEntity.builder()
        .id(subject.getId())
        .name(subject.getName())
        .description(subject.getDescription())
        .course(courseEntity)
        .professor(UserEntity.toEntity(subject.getProfessor()))
        .build();
    var schedules = subject.getSchedules().stream()
        .map(schedule -> ScheduleEntity.toEntity(schedule, entity)).collect(Collectors.toList());
    entity.setSchedules(schedules);
    return entity;
  }

  public static Subject toDomain(SubjectEntity entity, Course course) {
    var schedules = entity.getSchedules().stream()
        .map(ScheduleEntity::toDomain).collect(Collectors.toList());
    return new Subject(
        entity.getId(),
        entity.getName(),
        entity.getDescription(),
        course,
        schedules,
        UserEntity.toDomain(entity.getProfessor()));
  }
}
