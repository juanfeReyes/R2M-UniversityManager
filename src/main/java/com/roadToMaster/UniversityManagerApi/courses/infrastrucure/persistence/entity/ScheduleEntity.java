package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

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
public class ScheduleEntity {

  @Id
  @GeneratedValue(generator = "UUID")
  @GenericGenerator(
      name = "UUID",
      strategy = "org.hibernate.id.UUIDGenerator"
  )
  private String id;

  @Column
  private String dayOfWeek;

  @Column
  private LocalTime startDate;

  @Column
  private LocalTime endDate;

  @Column
  private String description;

  @ManyToOne
  @JoinColumn(name = "subject_id",
      foreignKey = @ForeignKey(name = "SUBJECT_ID_FK"))
  private SubjectEntity subject;
}
