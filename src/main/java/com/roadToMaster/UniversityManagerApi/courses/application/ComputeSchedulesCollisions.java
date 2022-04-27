package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.DayEnum;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;

@Service
public class ComputeSchedulesCollisions {

  private final SubjectRepository subjectRepository;

  private final ScheduleRepository scheduleRepository;

  private final CoursesEntityMapper entityMapper;

  public ComputeSchedulesCollisions(SubjectRepository subjectRepository, ScheduleRepository scheduleRepository, CoursesEntityMapper entityMapper) {
    this.subjectRepository = subjectRepository;
    this.scheduleRepository = scheduleRepository;
    this.entityMapper = entityMapper;
  }

  public List<Schedule> execute(List<Schedule> newSchedules, List<Schedule> oldSchedules){
    Map<DayEnum, List<Schedule>> schedulesByDay = Stream
        .concat(oldSchedules.stream(), newSchedules.stream())
        .collect(groupingBy(Schedule::getDay));

    return schedulesByDay.keySet().stream()
        .flatMap((day) -> getOverlappedSchedules(schedulesByDay.get(day)).stream())
        .collect(Collectors.toList());
  }

  private List<Schedule> getOverlappedSchedules(List<Schedule> schedulesByDay) {
    var conflictedSchedules = new ArrayList<Schedule>();
    schedulesByDay.sort(Comparator.comparing(s -> s.getStartTime()));
    for (int i = 0; i < schedulesByDay.size() - 1; i++) {
      if (schedulesByDay.get(i).isScheduleOverlapped(schedulesByDay.get(i + 1))) {
        conflictedSchedules.add(schedulesByDay.get(i));
      }
    }
    return conflictedSchedules;
  }

  public List<Schedule> getProfessorSchedules(String username) {
    var professorSubjectsIds = subjectRepository.findByProfessorUsername(username)
        .stream().map(SubjectEntity::getId).collect(Collectors.toList());
    return scheduleRepository.findBySubjectId(professorSubjectsIds).stream()
        .map(entityMapper::scheduleToDomain).collect(Collectors.toList());
  }

  public List<Schedule> getStudentSchedules(String studentId) {
    var professorSubjectsIds = subjectRepository.findAllByStudent(studentId)
        .stream().map(SubjectEntity::getId).collect(Collectors.toList());
    return scheduleRepository.findBySubjectId(professorSubjectsIds).stream()
        .map(entityMapper::scheduleToDomain).collect(Collectors.toList());
  }
}
