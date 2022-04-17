package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.ErrorResponse;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.util.UriComponentsBuilder;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;

public class GetCoursesTest extends ComponentTestBase {

  public static final String COURSE_URL = "/course";
  private static final ParameterizedTypeReference<PageResponse<CourseResponse>> courseResponseReference = new ParameterizedTypeReference<>() {
  };
  @Autowired
  public CourseRepository courseRepository;

  @Autowired
  public SubjectRepository subjectRepository;

  @Autowired
  public ScheduleRepository scheduleRepository;
  BiPredicate<Timestamp, Date> dateComparator = (expected, actual) -> expected.toInstant().compareTo(actual.toInstant()) == 0;
  @Autowired
  private CoursesEntityMapper entityMapper;

  @AfterEach
  public void teardown() {
    scheduleRepository.deleteAll();
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
  }

  @Test
  public void shouldGetCourses() {
    var course = CourseMother.validCourse();
    courseRepository.save(entityMapper.courseToEntity(course));

    var url = UriComponentsBuilder.fromUriString(COURSE_URL)
        .queryParam("pageNumber", 0)
        .queryParam("pageSize", 2)
        .toUriString();
    var pageResponse = restTemplate.exchange(url, HttpMethod.GET, null, courseResponseReference);

    assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(pageResponse.getBody().getContent()).first()
        .usingRecursiveComparison().ignoringFields("id").isEqualTo(course);
  }

  @Test
  public void shouldReturnBadRequestWhenPageNumberIsNegative() {
    var url = UriComponentsBuilder.fromUriString(COURSE_URL)
        .queryParam("pageNumber", -1)
        .queryParam("pageSize", 2)
        .toUriString();
    var pageResponse = restTemplate.exchange(url, HttpMethod.GET, null, ErrorResponse.class);

    assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(pageResponse.getBody().getFieldsError()).containsExactly("page number must be more at least 0");
  }

  @Test
  public void shouldReturnBadRequestWhenPageSizeIsZero() {
    var url = UriComponentsBuilder.fromUriString(COURSE_URL)
        .queryParam("pageNumber", 5)
        .queryParam("pageSize", 0)
        .toUriString();
    var pageResponse = restTemplate.exchange(url, HttpMethod.GET, null, ErrorResponse.class);

    assertThat(pageResponse.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(pageResponse.getBody().getFieldsError()).containsExactly("page size must be more at least 1");
  }
}
