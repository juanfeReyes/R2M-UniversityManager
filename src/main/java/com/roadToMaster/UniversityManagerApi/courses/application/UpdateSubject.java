package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IUpdateSubject;
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
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UpdateSubject implements IUpdateSubject {

  public static final String STUDENT_CONFLICT_ERROR_MSG = "There are %s student already registered, could generate schedules conflicts";
  private final ScheduleRepository scheduleRepository;

  private final SubjectRepository subjectRepository;

  private final CourseRepository courseRepository;

  private final CoursesEntityMapper entityMapper;

  private final UserEntityMapper userEntityMapper;

  private final UserRepository userRepository;

  private final ComputeSchedulesCollisions computeOverlappedSchedules;

  @Autowired
  public UpdateSubject(ScheduleRepository scheduleRepository, SubjectRepository subjectRepository, CourseRepository courseRepository, CoursesEntityMapper entityMapper, UserEntityMapper userEntityMapper, UserRepository userRepository, ComputeSchedulesCollisions computeOverlappedSchedules) {
    this.scheduleRepository = scheduleRepository;
    this.subjectRepository = subjectRepository;
    this.courseRepository = courseRepository;
    this.entityMapper = entityMapper;
    this.userEntityMapper = userEntityMapper;
    this.userRepository = userRepository;
    this.computeOverlappedSchedules = computeOverlappedSchedules;
  }

  @Transactional
  public Subject execute(String id, String name, String description,
                         String professorUsername, List<Schedule> schedules) {

    var user = userRepository.findByUsername(professorUsername);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Professor with username: %s was not found", professorUsername));
    }

    var subjectEntity = subjectRepository.findById(id).get();
    var studentsCount = subjectEntity.getStudents().size();
    if (studentsCount > 0) {
      throw new ResourceConflictException(String.format(STUDENT_CONFLICT_ERROR_MSG, studentsCount));
    }

    var course = entityMapper.courseToDomain(subjectEntity.getCourse(), Collections.emptyList());
    if (!subjectRepository.findByNameAndCourse(name, course.getId()).get().getId().equals(subjectEntity.getId())) {
      throw new ResourceAlreadyCreatedException(String.format("Subject within the course: %s and with name: %s already exists", course.getId(), name));
    }

    scheduleRepository.deleteBySubjectId(id);
    var overlappedSchedules = computeOverlappedSchedules.execute(schedules, computeOverlappedSchedules.getProfessorSchedules(professorUsername));
    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException("Cannot create subject schedules overlap with professors schedules", overlappedSchedules);
    }

    var professor = userEntityMapper.userToDomain(user.get());
    var subject = new Subject(id, name, description, schedules, Collections.emptyList(), professor, course);
    var savedSubject = subjectRepository.save(entityMapper.subjectToEntity(subject, subjectEntity.getCourse()));
    scheduleRepository.saveAll(schedules.stream().map(s -> entityMapper.scheduleToEntity(s, savedSubject)).collect(Collectors.toList()));

    return subject;
  }
}
