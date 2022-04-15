package com.roadToMaster.UniversityManagerApi.courses.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@AllArgsConstructor
public class Schedule {

  //TODO add validation that endDate > startDate

  private String id;

  private DayEnum day;

  private LocalTime startTime;

  private LocalTime endTime;

  private String description;

  public static List<Schedule> computeOverlappedSchedules(List<Schedule> newSchedules, List<Schedule> oldSchedules) {
    return newSchedules.stream()
        .filter(schedule -> !oldSchedules.stream()
            .filter(oldSchedule -> oldSchedule.isScheduleOverlapped(schedule))
            .collect(Collectors.toList()).isEmpty())
        .collect(Collectors.toList());
  }

  public boolean isScheduleOverlapped(Schedule newSchedule) {
    if (!this.day.equals(newSchedule.day)) {
      return false;
    }
    var startHours = startTime.truncatedTo(ChronoUnit.HOURS);
    var endHours = endTime.truncatedTo(ChronoUnit.HOURS);
    var newStartHours = newSchedule.startTime;
    var newEndHours = newSchedule.endTime;

    return (startHours.equals(newStartHours) || startHours.isAfter(newStartHours) && startHours.isBefore(newEndHours)) ||
        (endHours.isAfter(newStartHours) && endHours.isBefore(newEndHours) || endHours.equals(newEndHours));
  }
}
