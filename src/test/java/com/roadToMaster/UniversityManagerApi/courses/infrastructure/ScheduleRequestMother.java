package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.courses.domain.DayEnum;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.ScheduleRequest;

import java.time.format.DateTimeFormatter;

public class ScheduleRequestMother {

  public static ScheduleRequest buildFrom(Schedule schedule) {
    return new ScheduleRequest(
        schedule.getDay().name(),
        schedule.getStartTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        schedule.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")),
        schedule.getDescription()
    );
  }

  public static ScheduleRequest buildValid() {
    return new ScheduleRequest(
        DayEnum.wednesday.name(),
        "11:00",
        "13:32",
        "no lunch class"
    );
  }
}
