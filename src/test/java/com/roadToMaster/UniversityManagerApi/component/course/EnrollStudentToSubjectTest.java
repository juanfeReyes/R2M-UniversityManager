package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.SubjectRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.ErrorResponse;
import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.UUID;

import static com.roadToMaster.UniversityManagerApi.courses.application.EnrollStudentToSubject.SCHEDULE_COLLISION_MSG;
import static org.assertj.core.api.Assertions.assertThat;

public class EnrollStudentToSubjectTest extends ComponentTestBase {

  private static final String ENROLL_STUDENT_URL = "/student/{username}/subject/{subjectId}";

  @Autowired
  private CoursesEntityMapper entityMapper;

  @Autowired
  private UserEntityMapper userEntityMapper;

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private SessionFactory sessionFactory;

  @AfterEach
  public void teardown() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void shouldEnrollStudentToSubject() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    var student = userEntityMapper.userToDomain(studentEntity);

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(ENROLL_STUDENT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        Void.class, student.getUsername(), expectedSubject.getId());

    var savedSubject = subjectRepository.findAllByStudent(student.getId());
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(savedSubject).first()
        .extracting("id").isEqualTo(expectedSubject.getId());
  }

  @Test
  public void shouldReturnSubjectNotFoundWhenEnrollStudent() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    var student = userEntityMapper.userToDomain(studentEntity);

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(ENROLL_STUDENT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        ErrorResponse.class, student.getUsername(), UUID.randomUUID().toString());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo("Subject does not exists");
  }

  @Test
  public void shouldReturnStudentNotFoundWhenEnrollStudent() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    userEntityMapper.userToDomain(studentEntity);

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(ENROLL_STUDENT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        ErrorResponse.class, "not-exists", expectedSubject.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo("Student does not exists");
  }

  @Test
  public void shouldReturnStudentConflictWhenEnrollStudent() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedule = ScheduleMother.buildSchedule(0, 10);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    var student = userEntityMapper.userToDomain(studentEntity);

    var subjectEntity = entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity);
    subjectEntity.getStudents().add(studentEntity);
    var actualStoredSubject = subjectRepository.save(subjectEntity);
    var expectedSubject = entityMapper.subjectToDomain(actualStoredSubject, List.of(schedule));
    scheduleRepository.save(entityMapper.scheduleToEntity(schedule, actualStoredSubject));

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(ENROLL_STUDENT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        ErrorResponse.class, student.getUsername(), expectedSubject.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody().getMessage()).isEqualTo(String.format(SCHEDULE_COLLISION_MSG, student.getUsername(), expectedSubject.getName()));
  }
}
