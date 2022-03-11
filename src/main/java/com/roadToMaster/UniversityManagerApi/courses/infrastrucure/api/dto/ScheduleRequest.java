package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.roadToMaster.UniversityManagerApi.courses.domain.DayEnum;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import io.swagger.v3.oas.annotations.media.Schema;
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

  @Schema(example = "friday", allowableValues = {
      "monday",
      "tuesday",
      "wednesday",
      "thursday",
      "friday",
      "saturday"})
  private String day;

  @Schema(example = "00:00")
  private String startHours;

  @Schema(example = "01:00")
  private String endHours;

  @Schema(example = "")
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
