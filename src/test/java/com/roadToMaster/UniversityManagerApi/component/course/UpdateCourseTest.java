package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.CourseRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;

public class UpdateCourseTest extends ComponentTestBase {

  public static final String COURSE_URL = "/course/{courseId}";
  @Autowired
  public CourseRepository courseRepository;
  BiPredicate<Timestamp, Date> dateComparator = (expected, actual) -> expected.toInstant().compareTo(actual.toInstant()) == 0;
  @Autowired
  private CoursesMapper mapper;
  @Autowired
  private CoursesEntityMapper entityMapper;

  @AfterEach
  public void teardown() {
    courseRepository.deleteAll();
  }

  @Test
  public void shouldUpdateCourse() {
    var courseEntity = courseRepository.save(entityMapper.courseToEntity(CourseMother.validCourse()));
    var request = CourseRequestMother.buildValidRequest();

    var response = restTemplate.exchange(COURSE_URL, HttpMethod.PUT,
        new HttpEntity<>(request), CourseResponse.class, courseEntity.getId());

    var courseStored = courseRepository.findByName(request.getName()).get();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).usingRecursiveComparison()
        .ignoringFields("subjects", "id").isEqualTo(request);
    assertThat(courseStored).usingRecursiveComparison()
        .ignoringFields("subjects", "id", "createdDate", "updatedDate")
        .withEqualsForFields(dateComparator, "startDate", "endDate")
        .isEqualTo(request);
  }
}
