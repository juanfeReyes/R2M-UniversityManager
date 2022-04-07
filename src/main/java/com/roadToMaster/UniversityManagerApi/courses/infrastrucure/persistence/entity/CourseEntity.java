package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

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
}
