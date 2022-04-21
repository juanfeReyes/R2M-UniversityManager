package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.CourseRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.ErrorResponse;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteCourseTest extends ComponentTestBase {

  public static final String COURSE_URL = "/course/{courseId}";

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private CourseRepository courseRepository;

  @Autowired
  private ScheduleRepository scheduleRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CoursesMapper mapper;

  @Autowired
  private CoursesEntityMapper entityMapper;

  @Autowired
  private UserEntityMapper userEntityMapper;

  @AfterEach
  public void teardown() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void shouldDeleteCourse() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));

    var response = restTemplate.exchange(COURSE_URL, HttpMethod.DELETE,
        null, CourseResponse.class, courseEntity.getId());

    var courseStored = courseRepository.findByName(courseEntity.getName());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(courseStored).isEmpty();
  }

  @Test
  public void shouldReturnNotFoundWhenCourseDoesNotExists() {
    var notExistingId = UUID.randomUUID().toString();
    var response = restTemplate.exchange(COURSE_URL, HttpMethod.DELETE,
        null, ErrorResponse.class, notExistingId);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo(String.format("Course with id: %s does not exists", notExistingId));
  }

  @Test
  public void shouldReturnConflictWhenCourseHasSubjects() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));;
    var professor = userEntityMapper.userToDomain(userEntity);
    subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));

    var response = restTemplate.exchange(COURSE_URL, HttpMethod.DELETE,
        null, ErrorResponse.class, courseEntity.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
    assertThat(response.getBody().getMessage()).isEqualTo(String.format("Course has %s subjects, must have none", 1));
  }
}
