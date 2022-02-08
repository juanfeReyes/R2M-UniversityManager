package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequest;

import java.util.Date;
import java.util.UUID;

public class CourseRequestMother {

  public static CourseRequest buildValidRequest() {
    var request = new CourseRequest();
    request.setActive(true);
    request.setId(UUID.randomUUID().toString());
    request.setName("Valid course");
    request.setEndDate(new Date());
    request.setStartDate(new Date());

    return request;
  }

  public static CourseRequest buildInvalidRequestByName() {
    var request = new CourseRequest();
    request.setActive(true);
    request.setId(UUID.randomUUID().toString());
    request.setName("");
    request.setEndDate(new Date());
    request.setStartDate(new Date());

    return request;
  }
}
