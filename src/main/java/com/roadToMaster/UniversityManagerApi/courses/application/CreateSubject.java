package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.ICreateSubject;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
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

  private final ComputeSchedulesCollisions computeOverlappedSchedules;

  @Autowired
  public CreateSubject(CoursesEntityMapper entityMapper, UserEntityMapper userEntityMapper, UserRepository userRepository, CourseRepository courseRepository, SubjectRepository subjectRepository, ScheduleRepository scheduleRepository, ComputeSchedulesCollisions computeOverlappedSchedules) {
    this.entityMapper = entityMapper;
    this.userEntityMapper = userEntityMapper;
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.subjectRepository = subjectRepository;
    this.scheduleRepository = scheduleRepository;
    this.computeOverlappedSchedules = computeOverlappedSchedules;
  }

  @Transactional
  public Subject execute(String name, String description, String courseId,
                         String professorUsername, List<Schedule> schedules) {

    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Course with id %s does not exists", courseId));
    }
    var course = entityMapper.courseToDomain(courseEntity.get(), Collections.emptyList());

    if (subjectRepository.findByNameAndCourse(name, courseId).isPresent()) {
      throw new ResourceAlreadyCreatedException(String.format("Subject within the course: %s and with name: %s already exists", courseId, name));
    }

    var user = userRepository.findByUsername(professorUsername);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Professor with username: %s was not found", professorUsername));
    }
    if(!user.get().getRole().equals(RoleEnum.PROFESSOR.value)){
      throw new ResourceConflictException("User is not a professor, cannot be assign to Subject");
    }

    var overlappedSchedules = computeOverlappedSchedules.execute(schedules, computeOverlappedSchedules.getProfessorSchedules(professorUsername));
    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException("Cannot create subject schedules overlap with professors schedules", overlappedSchedules);
    }

    var professor = userEntityMapper.userToDomain(user.get());
    var subject = new Subject(null, name, description, schedules, Collections.emptyList(), professor, course);
    var savedSubject = subjectRepository.save(entityMapper.subjectToEntity(subject, courseEntity.get()));
    var savedSchedules = scheduleRepository.saveAll(schedules.stream().map(s -> entityMapper.scheduleToEntity(s, savedSubject)).collect(Collectors.toList()));
    savedSubject.setSchedules(savedSchedules);

    return entityMapper.subjectToDomain(savedSubject, savedSchedules.stream().map(entityMapper::scheduleToDomain).collect(Collectors.toList()));
  }

}
