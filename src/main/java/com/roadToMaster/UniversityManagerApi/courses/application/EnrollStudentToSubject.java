package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IEnrollStudentToSubject;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class EnrollStudentToSubject implements IEnrollStudentToSubject {

  public static final String SCHEDULE_COLLISION_MSG = "Student %s cannot enroll to subject: %s due to schedules conflict";

  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

  private final CoursesEntityMapper coursesEntityMapper;

  private final ComputeSchedulesCollisions computeOverlappedSchedules;

  @Autowired
  public EnrollStudentToSubject(SubjectRepository subjectRepository, UserRepository userRepository, CoursesEntityMapper coursesEntityMapper, ComputeSchedulesCollisions computeOverlappedSchedules) {
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
    this.coursesEntityMapper = coursesEntityMapper;
    this.computeOverlappedSchedules = computeOverlappedSchedules;
  }

  public boolean execute(String subjectId, String username) {
    var subjectEntity = subjectRepository.findById(subjectId);
    if (subjectEntity.isEmpty()) {
      throw new ResourceNotFoundException("Subject does not exists");
    }

    var studentEntity = userRepository.findByUsername(username);
    if (studentEntity.isEmpty()) {
      throw new ResourceNotFoundException("Student does not exists");
    }

    var subject = subjectEntity.get();
    var student = studentEntity.get();

    if(!student.getRole().equals(RoleEnum.STUDENT.value)){
      throw new ResourceConflictException("User is not a student, cannot be assign to Subject");
    }

    var currentStudentSchedules = computeOverlappedSchedules.getStudentSchedules(student.getId());
    var subjectSchedules = subject.getSchedules().stream().map(coursesEntityMapper::scheduleToDomain).collect(Collectors.toList());
    var overlappedSchedules = computeOverlappedSchedules.execute(subjectSchedules, currentStudentSchedules);
    if (!overlappedSchedules.isEmpty()) {
      throw new ScheduleConflictException(String.format(SCHEDULE_COLLISION_MSG, student.getUsername(), subject.getName()), overlappedSchedules);
    }

    subject.getStudents().add(studentEntity.get());
    subjectRepository.save(subject);

    return true;
  }
}
