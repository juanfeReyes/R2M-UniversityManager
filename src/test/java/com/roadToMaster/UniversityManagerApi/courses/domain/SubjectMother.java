package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;
import com.roadToMaster.UniversityManagerApi.users.domain.User;

import java.util.List;

public class SubjectMother {

  public static Subject validSubject(Course course, User professor, List<Schedule> schedules) {
    var faker = FakerUtil.buildFaker();
    return new Subject(
        faker.internet().uuid(),
        faker.artist().name(),
        faker.university().name(),
        course,
        schedules,
        professor);
  }
}
