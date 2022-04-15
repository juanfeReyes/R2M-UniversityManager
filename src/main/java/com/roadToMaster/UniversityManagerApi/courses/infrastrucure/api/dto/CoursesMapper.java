package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CoursesMapper {

  @Mapping(target = "subjects", expression = "java(List.of())")
  Course courseRequestToCourse(CourseRequest courseRequest);

  CourseResponse courseToResponse(Course course);

  @Mapping(target = "subjects", expression = "java(List.of())")
  @Mapping(target = "id", source = "courseId")
  Course courseRequestToCourse(CourseRequest courseRequest, String courseId);

  @Mapping(target = "startTime", source = "startHours", dateFormat = "HH:mm")
  @Mapping(target = "endTime", source = "endHours", dateFormat = "HH:mm")
  Schedule scheduleRequestToSchedule(ScheduleRequest request);

  @Mapping(target = "professorUsername", source = "professor.username")
  @Mapping(target = "courseId", source = "course.id")
  @Mapping(target = "schedules", source = "schedules")
  SubjectResponse subjectToResponse(Subject subject);
}
