package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;
import com.roadToMaster.UniversityManagerApi.users.domain.User;

import java.util.Collections;
import java.util.List;

public class SubjectMother {

  public static Subject validSubject(User professor, List<Schedule> schedules, Course course) {
    var faker = FakerUtil.buildFaker();
    return new Subject(
        faker.internet().uuid(),
        faker.artist().name(),
        faker.university().name(),
        schedules,
        Collections.emptyList(),
        professor,
        course);
  }
}
