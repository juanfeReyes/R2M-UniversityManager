package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class CourseRequest {

  private String id;

  private String name;

  private Date startDate;

  private Date endDate;

  private boolean active;
}
