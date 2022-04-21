package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import lombok.Getter;

import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

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

  public static List<Schedule> computeOverlappedSchedules(List<Schedule> newSchedules, List<Schedule> oldSchedules) {
    Map<DayEnum, List<Schedule>> schedulesByDay = Stream
        .concat(oldSchedules.stream(), newSchedules.stream())
        .collect(groupingBy(Schedule::getDay));

    return schedulesByDay.keySet().stream()
        .flatMap((day) -> getOverlappedSchedules(schedulesByDay.get(day)).stream())
        .collect(Collectors.toList());
  }

  private static List<Schedule> getOverlappedSchedules(List<Schedule> schedulesByDay) {
    var conflictedSchedules = new ArrayList<Schedule>();
    schedulesByDay.sort(Comparator.comparing(s -> s.startTime));
    for (int i = 0; i < schedulesByDay.size() - 1; i++) {
      if (schedulesByDay.get(i).isScheduleOverlapped(schedulesByDay.get(i + 1))) {
        conflictedSchedules.add(schedulesByDay.get(i));
      }
    }
    return conflictedSchedules;
  }

  private boolean isScheduleOverlapped(Schedule newSchedule) {
    var endHours = endTime.truncatedTo(ChronoUnit.HOURS);
    var newStartHours = newSchedule.startTime;

    return endHours.isAfter(newStartHours);
  }
}
