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
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import javax.persistence.EntityManager;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateSubjectTest extends ComponentTestBase {

  private static final String SUBJECT_URL = "/course/{courseName}/subject";

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

  @BeforeEach
  public void init() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    userRepository.deleteAll();
  }

  @Test
  public void ShouldCreateSubject() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var expectedSubject = SubjectMother.validSubject(professor, schedules);

    userRepository.save(userEntityMapper.userToEntity(professor));
    courseRepository.save(entityMapper.courseToEntity(course));

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.POST, new HttpEntity<>(request),
        Void.class, course.getName());

    var savedSubject = subjectRepository.findByProfessorUsername(professor.getUsername());
    var savedSchedules = scheduleRepository.findBySubjectId(savedSubject.stream().map(SubjectEntity::getId).collect(Collectors.toList()));
    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(savedSubject).first().usingRecursiveComparison()
        .ignoringFields("course", "professor", "schedules").isEqualTo(expectedSubject);
    assertThat(savedSchedules.stream().map(entityMapper::scheduleToDomain).collect(Collectors.toList()))
        .usingRecursiveComparison().ignoringFields("id").isEqualTo(schedules);
  }
}
