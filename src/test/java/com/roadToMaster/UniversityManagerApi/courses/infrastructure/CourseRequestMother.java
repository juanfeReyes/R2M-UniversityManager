package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequestDTO;

import java.util.Date;
import java.util.UUID;

public class CourseRequestMother {

  public static CourseRequestDTO buildValidRequest(){
    var request = new CourseRequestDTO();
    request.setActive(true);
    request.setId(UUID.randomUUID().toString());
    request.setName("Valid course");
    request.setEndDate(new Date());
    request.setStartDate(new Date());

    return request;
  }

  public static CourseRequestDTO buildInvalidRequestByName(){
    var request = new CourseRequestDTO();
    request.setActive(true);
    request.setId(UUID.randomUUID().toString());
    request.setName("");
    request.setEndDate(new Date());
    request.setStartDate(new Date());

    return request;
  }
}
