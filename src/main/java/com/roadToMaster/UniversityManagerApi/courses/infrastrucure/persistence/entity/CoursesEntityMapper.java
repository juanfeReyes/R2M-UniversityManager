package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import org.mapstruct.CollectionMappingStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring", collectionMappingStrategy = CollectionMappingStrategy.ADDER_PREFERRED)
public interface CoursesEntityMapper {

  //Schedules

  // to domain
  @Mapping(target = "startTime", source = "startDate")
  @Mapping(target = "endTime", source = "endDate")
  @Mapping(target = "day", source = "dayOfWeek")
  Schedule scheduleToDomain(ScheduleEntity entity);

  // to entity
  @Mapping(target = "startDate", source = "schedule.startTime")
  @Mapping(target = "endDate", source = "schedule.endTime")
  @Mapping(target = "dayOfWeek", source = "schedule.day")
  @Mapping(target = "id", source = "schedule.id")
  @Mapping(target = "description", source = "schedule.description")
  ScheduleEntity scheduleToEntity(Schedule schedule, SubjectEntity subject);

  // Subject
  // to domain
  @Mapping(target = "schedules", source = "schedules")
  Subject subjectToDomain(SubjectEntity entity, List<Schedule> schedules);

  // to entity
  @Mapping(target = "id", source = "subject.id")
  @Mapping(target = "name", source = "subject.name")
  @Mapping(target = "course", source = "course")
  SubjectEntity subjectToEntity(Subject subject, CourseEntity course);

  CourseEntity courseToEntity(Course course);

  Course courseToDomain(CourseEntity courseEntity, List<Subject> subjects);

}
