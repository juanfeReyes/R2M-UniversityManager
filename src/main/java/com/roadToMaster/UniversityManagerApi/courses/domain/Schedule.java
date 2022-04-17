package com.roadToMaster.UniversityManagerApi.courses.domain;

import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.apache.tomcat.jni.Local;

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

  private String id;

  private DayEnum day;

  private LocalTime startTime;

  private LocalTime endTime;

  private String description;

  public Schedule(String id, DayEnum day, LocalTime startTime, LocalTime endTime, String description){
    this.id = id;
    this.day = day;
    this.startTime = startTime;
    this.endTime = endTime.minusMinutes(1);
    this.description = description;

    if(endTime.isAfter(startTime)){
      throw new ResourceConflictException("Schedule end time should be after start time");
    }
  }
  
  public static List<Schedule> computeOverlappedSchedules(List<Schedule> newSchedules, List<Schedule> oldSchedules) {
    var conflictedSchedules = new ArrayList<Schedule>();

    Map<DayEnum, List<Schedule>> schedulesByDay = Stream
        .concat(oldSchedules.stream(), newSchedules.stream())
        .collect(groupingBy(Schedule::getDay));
    // for each day sort by start date

    schedulesByDay.keySet().stream().forEach((day) -> {
      var schedulesDay = schedulesByDay.get(day);
      schedulesDay.sort(Comparator.comparing(s -> s.startTime));
      for(int i = 0; i < schedulesDay.size()-1; i++){
        if(schedulesDay.get(i).isScheduleOverlapped(schedulesDay.get(i+1))){
          conflictedSchedules.add(schedulesDay.get(i));
        }
      }
    });

    return conflictedSchedules;
  }

  private boolean isScheduleOverlapped(Schedule newSchedule) {
    var endHours = endTime.truncatedTo(ChronoUnit.HOURS);
    var newStartHours = newSchedule.startTime;

    return endHours.isAfter(newStartHours);
  }
}
