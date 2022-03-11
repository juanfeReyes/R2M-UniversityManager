package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CreateSubject implements ICreateSubject {

  private final UserRepository userRepository;

  private final CourseRepository courseRepository;

  private final SubjectRepository subjectRepository;

  @Autowired
  public CreateSubject(UserRepository userRepository, CourseRepository courseRepository, SubjectRepository subjectRepository) {
    this.userRepository = userRepository;
    this.courseRepository = courseRepository;
    this.subjectRepository = subjectRepository;
  }

  //TODO: Clean this monster
  @Transactional
  public Subject execute(String id, String name, String description, String courseName,
                         String professorUsername, List<Schedule> schedules) {
    if (subjectRepository.existsById(id)) {
      throw new ResourceAlreadyCreatedException(String.format("Subject already exists with id: %s", id));
    }

    var uax = courseRepository.findAll();
    var courseEntity = courseRepository.findByName(courseName);
    if (courseEntity.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Course with name %s does not exists", courseName));
    }
    var course = CourseEntity.toDomain(courseEntity.get());

    //Search user must have rol professor
    var user = userRepository.findByUsername(professorUsername);
    if (user.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Professor with username: %s was not found", professorUsername));
    }
    var professor = UserEntity.toDomain(user.get());

    //TODO: this should be at infrastructure level
    //Find all subjects of professor and extract schedules
    var professorSubjects = subjectRepository.findByProfessorUsername(professor.getUsername())
        .stream().map(subject -> SubjectEntity.toDomain(subject, course)).collect(Collectors.toList());
    var professorSchedules = professorSubjects.stream()
        .flatMap(subject -> subject.getSchedules().stream())
        .collect(Collectors.toList());

    //Grant that current schedules does not overlap with professor schedules
    var overlappedSchedules = computeOverlappedSchedules(schedules, professorSchedules);

    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException("Cannot create subject schedules overlap with professors schedules", overlappedSchedules);
    }

    //Add subjects
    var subject = new Subject(id, name, description, course, schedules, professor);
    subjectRepository.save(SubjectEntity.toEntity(subject, courseEntity.get()));

    return subject;
  }

  private List<Schedule> computeOverlappedSchedules(List<Schedule> newSchedules, List<Schedule> oldSchedules) {
    return newSchedules.stream()
        .filter(schedule -> !oldSchedules.stream()
            .filter(oldSchedule -> oldSchedule.isScheduleOverlapped(schedule))
            .collect(Collectors.toList()).isEmpty())
        .collect(Collectors.toList());
  }
}
