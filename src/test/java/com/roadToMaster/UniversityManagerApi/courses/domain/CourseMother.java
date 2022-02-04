package com.roadToMaster.UniversityManagerApi.courses.domain;

import java.util.Date;
import java.util.UUID;

public class CourseMother {

  public static Course validCourse() {
    return new Course(
        UUID.randomUUID().toString(),
        "test name",
        new Date(),
        new Date(),
        Boolean.TRUE
    );
  }
}
