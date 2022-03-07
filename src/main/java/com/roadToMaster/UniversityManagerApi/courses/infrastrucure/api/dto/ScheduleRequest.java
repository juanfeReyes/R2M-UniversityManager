package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.roadToMaster.UniversityManagerApi.courses.domain.DayEnum;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ScheduleRequest {

  private static final String HOUR_PATTERN = "HH:mm";

  private String day;

  private String startHours;

  private String endHours;

  private String description;

  public static Schedule toDomain(ScheduleRequest request) {
    return new Schedule(
        UUID.randomUUID().toString(),
        DayEnum.valueOf(request.day),
        LocalTime.parse(request.startHours, DateTimeFormatter.ofPattern(HOUR_PATTERN)),
        LocalTime.parse(request.endHours, DateTimeFormatter.ofPattern(HOUR_PATTERN)),
        request.getDescription()
    );
  }
}
