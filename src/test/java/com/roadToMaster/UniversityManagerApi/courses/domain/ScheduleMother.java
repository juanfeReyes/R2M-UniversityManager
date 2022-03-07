package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.FakerUtil;

import java.time.LocalTime;

public class ScheduleMother {

  public static Schedule buildSchedule(long startOffset, long endOffset) {
    var faker = FakerUtil.buildFaker();
    var startDate = LocalTime.of(10, 0).minusMinutes(startOffset);
    var endDate = LocalTime.of(12, 30).minusMinutes(endOffset);
    return new Schedule(
        faker.internet().uuid(),
        DayEnum.friday,
        startDate,
        endDate,
        faker.job().title()
    );
  }
}
