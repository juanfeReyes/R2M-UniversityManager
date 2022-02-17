package com.roadToMaster.UniversityManagerApi.courses.domain;

public class SubjectMother {

  public static Subject validSubject(Course course) {
    return new Subject(
        "123",
        "modernism",
        "moder subject",
        course);
  }
}
