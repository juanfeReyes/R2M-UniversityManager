package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoursesMapper {

  @Mapping(target = "subjects", expression = "java(List.of())")
  Course courseRequestToCourse(CourseRequest courseRequest);

  @Mapping(target = "startTime", source = "startHours", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endHours", dateFormat = "HH:mm")
  Schedule scheduleRequestToSchedule(ScheduleRequest request);
}
