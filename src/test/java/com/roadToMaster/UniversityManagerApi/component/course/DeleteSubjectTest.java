package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class DeleteSubjectTest extends ComponentTestBase {

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
  public void ShouldDeleteSubject() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.DELETE, null,
        Void.class, expectedSubject.getId());

    var savedSubject = subjectRepository.findByProfessorUsername(professor.getUsername());
    var savedSchedules = scheduleRepository.findBySubjectId(savedSubject.stream().map(SubjectEntity::getId).collect(Collectors.toList()));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(savedSubject).isEmpty();
    assertThat(savedSchedules).isEmpty();
  }
}
