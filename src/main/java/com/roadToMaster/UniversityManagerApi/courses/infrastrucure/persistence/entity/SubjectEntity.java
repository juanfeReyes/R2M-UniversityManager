package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.shared.infrastructure.persistence.AuditMetadata;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "subject")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SubjectEntity extends AuditMetadata {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String id;

  @Column(nullable = false)
  private String name;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "course_id",
      foreignKey = @ForeignKey(name = "COURSE_ID_FK"))
  private CourseEntity course;

  @OneToOne
  @JoinColumn(name = "professor")
  private UserEntity professor;

  @OneToMany(mappedBy = "subject", cascade = CascadeType.REMOVE)
  private List<ScheduleEntity> schedules;

  @Column(nullable = false)
  @Type(type = "org.hibernate.type.NumericBooleanType")
  private boolean active;
}
