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
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
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

import javax.persistence.EntityManager;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateSubjectTest extends ComponentTestBase {

  private static final String SUBJECT_URL = "/subject/{subjectId}";

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
  private EntityManager entityManager;

  @AfterEach
  public void teardown() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void ShouldUpdateSubject() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));;
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        Void.class, expectedSubject.getId());

    var savedSubject = subjectRepository.findByProfessorUsername(professor.getUsername());
    var savedSchedules = scheduleRepository.findBySubjectId(savedSubject.stream().map(SubjectEntity::getId).collect(Collectors.toList()));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(savedSubject).first().usingRecursiveComparison()
        .ignoringFields("course", "professor", "schedules", "createdDate", "active", "updatedDate", "id").isEqualTo(expectedSubject);
    assertThat(savedSchedules.stream().map(entityMapper::scheduleToDomain).collect(Collectors.toList()))
        .usingRecursiveComparison().ignoringFields("id").isEqualTo(schedules);
  }

  @Test
  public void ShouldReturnNotFoundWhenCourseDoesNotExists() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));;
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var request = SubjectRequestMother.buildSubjectRequestWithRandomCourseId(expectedSubject);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        ErrorResponse.class, expectedSubject.getId());

   assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
   assertThat(response.getBody().getMessage()).isEqualTo(String.format("Course with id %s does not exists", request.getCourseId()));
  }

  @Test
  public void ShouldReturnNotFoundWhenProfessorDoesNotExists() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));;
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var request = SubjectRequestMother.buildSubjectRequestWithRandomProfessorUsername(expectedSubject);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.PUT, new HttpEntity<>(request),
        ErrorResponse.class, expectedSubject.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    assertThat(response.getBody().getMessage()).isEqualTo(String.format("Professor with username: %s was not found", request.getProfessorUserName()));
  }
}
