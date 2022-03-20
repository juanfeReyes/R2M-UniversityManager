package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.DayEnum;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
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
  @JoinColumn(name = "subject_name",
      foreignKey = @ForeignKey(name = "SUBJECT_NAME_FK"))
  private SubjectEntity subject;

  public static Schedule toDomain(ScheduleEntity entity) {
    return new Schedule(
        entity.getId(),
        DayEnum.valueOf(entity.getDayOfWeek()),
        entity.getStartDate(),
        entity.getEndDate(),
        entity.getDescription()
    );
  }

  public static ScheduleEntity toEntity(Schedule schedule, SubjectEntity subject) {
    return new ScheduleEntity(
        schedule.getId(),
        schedule.getDay().name(),
        schedule.getStartTime(),
        schedule.getEndTime(),
        schedule.getDescription(),
        subject
    );
  }
}
