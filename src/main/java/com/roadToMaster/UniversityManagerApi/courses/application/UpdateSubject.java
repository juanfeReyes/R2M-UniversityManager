package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IUpdateSubject;
import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
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
  public Subject execute(String id, String name, String description, String courseId,
                         String professorUsername, List<Schedule> schedules) {
    var courseEntity = courseRepository.findById(courseId);
    if (courseEntity.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Course with id %s does not exists", courseId));
    }
    var course = entityMapper.courseToDomain(courseEntity.get(), Collections.emptyList());

    var user = userRepository.findByUsername(professorUsername);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Professor with username: %s was not found", professorUsername));
    }

    subjectRepository.findById(id).ifPresent(subject -> {
      var studentsCount = subject.getStudents().size();
      if(subject.getStudents().size() > 0){
        throw new ResourceConflictException(String.format(STUDENT_CONFLICT_ERROR_MSG, studentsCount));
      }
    });

    scheduleRepository.deleteBySubjectId(id);
    var overlappedSchedules = computeOverlappedSchedules.execute(schedules, computeOverlappedSchedules.getProfessorSchedules(professorUsername));
    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException("Cannot create subject schedules overlap with professors schedules", overlappedSchedules);
    }

    var professor = userEntityMapper.userToDomain(user.get());
    var subject = new Subject(id, name, description, schedules, Collections.emptyList(), professor, course);
    var savedSubject = subjectRepository.save(entityMapper.subjectToEntity(subject, courseEntity.get()));
    scheduleRepository.saveAll(schedules.stream().map(s -> entityMapper.scheduleToEntity(s, savedSubject)).collect(Collectors.toList()));

    return subject;
  }
}
