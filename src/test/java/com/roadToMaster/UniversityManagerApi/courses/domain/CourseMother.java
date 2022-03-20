package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CourseMother {

  public static Course validCourse() {
    var faker = FakerUtil.buildFaker();
    return new Course(
        faker.internet().uuid(),
        faker.name().title(),
        new Date(),
        new Date(),
        Boolean.TRUE,
        List.of()
    );
  }
}
