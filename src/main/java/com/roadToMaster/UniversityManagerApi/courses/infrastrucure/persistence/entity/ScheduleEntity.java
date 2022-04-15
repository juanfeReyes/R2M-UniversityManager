package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.shared.infrastructure.persistence.AuditMetadata;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.time.LocalTime;

@Entity
@Table(name = "schedule")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduleEntity extends AuditMetadata {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String id;

  @Column(nullable = false)
  private String dayOfWeek;

  @Column(nullable = false)
  private LocalTime startDate;

  @Column(nullable = false)
  private LocalTime endDate;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "subject_id",
      foreignKey = @ForeignKey(name = "SUBJECT_ID_FK"))
  private SubjectEntity subject;
}
