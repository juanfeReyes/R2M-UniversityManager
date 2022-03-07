package com.roadToMaster.UniversityManagerApi.courses.domain.exceptions;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;

import java.util.List;

public class ScheduleConflictException extends ResourceConflictException {

  private final List<Schedule> conflictedSchedules;

  public ScheduleConflictException(String message, List<Schedule> conflictedSchedules) {
    super(message);
    this.conflictedSchedules = conflictedSchedules;
  }
}
