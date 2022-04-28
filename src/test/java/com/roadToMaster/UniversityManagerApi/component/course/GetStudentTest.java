package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.SubjectRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.StudentResponse;
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

import static org.assertj.core.api.Assertions.assertThat;

public class GetStudentTest extends ComponentTestBase {

  private static final String GET_STUDENT_URL = "/student/{studentUsername}";

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

  @AfterEach
  public void teardown() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void shouldGetStudentToSubject() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    var student = userEntityMapper.userToDomain(studentEntity);

    var subjectEntity = entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity);
    subjectEntity.getStudents().add(studentEntity);
    var actualStoredSubject = subjectRepository.save(subjectEntity);

    var response = restTemplate.exchange(GET_STUDENT_URL, HttpMethod.GET, null,
        StudentResponse.class, student.getUsername());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody().getProfileInformation()).usingRecursiveComparison().isEqualTo(student);
    assertThat(response.getBody().getSubjects()).first()
        .usingRecursiveComparison().ignoringFields("professorUsername", "courseId").isEqualTo(actualStoredSubject);
  }

  @Test
  public void shouldReturnNotFoundGetStudentToSubject() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);

    var studentEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValidWithRole(RoleEnum.STUDENT)));
    userEntityMapper.userToDomain(studentEntity);

    var subjectEntity = entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity);
    subjectEntity.getStudents().add(studentEntity);

    var response = restTemplate.exchange(GET_STUDENT_URL, HttpMethod.GET, null,
        ErrorResponse.class, "not-exists");

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo("Student does not exists");
  }
}
