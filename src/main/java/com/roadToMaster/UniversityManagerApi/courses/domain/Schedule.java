package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import lombok.Getter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

@Getter
public class Schedule {

  private final String id;

  private final DayEnum day;

  private final LocalTime startTime;

  private final LocalTime endTime;

  private final String description;

  public Schedule(String id, DayEnum day, LocalTime startTime, LocalTime endTime, String description) {
    this.id = id;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime;
    this.description = description;

    if (endTime.isBefore(startTime)) {
      throw new ResourceConflictException("Schedule end time should be after start time");
    }
  }

  public boolean isScheduleOverlapped(Schedule newSchedule) {
    var endHours = endTime.truncatedTo(ChronoUnit.HOURS);
    var newStartHours = newSchedule.startTime;

    return endHours.isAfter(newStartHours);
  }
}
