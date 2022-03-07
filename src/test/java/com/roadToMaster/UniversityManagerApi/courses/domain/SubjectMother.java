package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.users.domain.User;

import java.util.List;

public class SubjectMother {

  public static Subject validSubject(Course course, User professor, List<Schedule> schedules) {
    return new Subject(
        "123",
        "modernism",
        "moder subject",
        course,
        schedules,
        professor);
  }
}
