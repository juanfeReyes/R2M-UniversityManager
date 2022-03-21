package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateSubject implements ICreateSubject {

  private final CoursesEntityMapper entityMapper;

  private final UserEntityMapper userEntityMapper;

  private final UserRepository userRepository;

  private final CourseRepository courseRepository;

  private final SubjectRepository subjectRepository;

  private final ScheduleRepository scheduleRepository;

  @Autowired
  public CreateSubject(CoursesEntityMapper entityMapper, UserEntityMapper userEntityMapper, UserRepository userRepository, CourseRepository courseRepository, SubjectRepository subjectRepository, ScheduleRepository scheduleRepository) {
    this.entityMapper = entityMapper;
    this.userEntityMapper = userEntityMapper;
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.subjectRepository = subjectRepository;
    this.scheduleRepository = scheduleRepository;
  }

  @Transactional
  public Subject execute(String id, String name, String description, String courseName,
                         String professorUsername, List<Schedule> schedules) {
    if (subjectRepository.existsById(id)) {
      throw new ResourceAlreadyCreatedException(String.format("Subject already exists with id: %s", id));
    }

    var courseEntity = courseRepository.findByName(courseName);
    if (courseEntity.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Course with name %s does not exists", courseName));
    }

    var user = userRepository.findByUsername(professorUsername);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Professor with username: %s was not found", professorUsername));
    }

    var professor = userEntityMapper.userToDomain(user.get());
    var professorSchedules = getProfessorSchedules(professor);

    var overlappedSchedules = computeOverlappedSchedules(schedules, professorSchedules);

    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException("Cannot create subject schedules overlap with professors schedules", overlappedSchedules);
    }

    var subject = new Subject(id, name, description, schedules, professor);
    var savedSubject = subjectRepository.save(entityMapper.subjectToEntity(subject, courseEntity.get()));
    scheduleRepository.saveAll(schedules.stream().map(s -> entityMapper.scheduleToEntity(s, savedSubject)).collect(Collectors.toList()));

    return subject;
  }

  private List<Schedule> getProfessorSchedules(User professor) {
    var professorSubjectsIds = subjectRepository.findByProfessorUsername(professor.getUsername())
        .stream().map(SubjectEntity::getId).collect(Collectors.toList());
    return scheduleRepository.findBySubjectId(professorSubjectsIds).stream()
        .map(entityMapper::scheduleToDomain).collect(Collectors.toList());
  }

  private List<Schedule> computeOverlappedSchedules(List<Schedule> newSchedules, List<Schedule> oldSchedules) {
    return newSchedules.stream()
        .filter(schedule -> !oldSchedules.stream()
            .filter(oldSchedule -> oldSchedule.isScheduleOverlapped(schedule))
            .collect(Collectors.toList()).isEmpty())
        .collect(Collectors.toList());
  }
}
