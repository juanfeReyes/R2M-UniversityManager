package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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
}
