package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

public class GetSubjectTest extends ComponentTestBase {

  private static final String SUBJECT_URL = "/subject";

  private static final ParameterizedTypeReference<PageResponse<SubjectResponse>> subjectResponseReference = new ParameterizedTypeReference<>() {
  };

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
  public void ShouldGetSubjects() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var course = entityMapper.courseToDomain(courseEntity, List.of());
    var userEntity = userRepository.save(userEntityMapper.userToEntity(UserMother.buildValid()));
    var professor = userEntityMapper.userToDomain(userEntity);
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subjectEntity = subjectRepository.save(entityMapper.subjectToEntity(SubjectMother.validSubject(professor, List.of(), course), courseEntity));
    var expectedSubject = entityMapper.subjectToDomain(subjectEntity, schedules);

    var url = UriComponentsBuilder.fromUriString(SUBJECT_URL)
        .queryParam("pageNumber", 0)
        .queryParam("pageSize", 2)
        .toUriString();
    var pageResponse = restTemplate.exchange(url, HttpMethod.GET, null, subjectResponseReference);

    assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(pageResponse.getBody().getContent()).first()
        .usingRecursiveComparison().ignoringFields("id", "professorUsername", "courseId", "schedules").isEqualTo(expectedSubject);
    assertThat(pageResponse.getBody().getContent()).extracting("professorUsername", "courseId")
        .contains(tuple(professor.getUsername(), course.getId()));
  }
}
