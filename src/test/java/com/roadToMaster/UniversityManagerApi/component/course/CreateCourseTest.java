package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.CourseRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.sql.Timestamp;
import java.util.Date;
import java.util.function.BiPredicate;

import static org.assertj.core.api.Assertions.assertThat;


public class CreateCourseTest extends ComponentTestBase {

  public static final String COURSE_URL = "/course";
  @Autowired
  public CourseRepository courseRepository;
  BiPredicate<Timestamp, Date> dateComparator = (expected, actual) -> expected.toInstant().compareTo(actual.toInstant()) == 0;
  @Autowired
  private CoursesMapper mapper;
  @Autowired
  private CoursesEntityMapper entityMapper;

  @BeforeEach
  public void init() {
    courseRepository.deleteAll();
  }

  @Test
  public void shouldCreateCourse() {
    var request = CourseRequestMother.buildValidRequest();
    var response = restTemplate.postForEntity(COURSE_URL, request, Course.class);

    var courseStored = courseRepository.findByName(request.getName()).get();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(response.getBody()).usingRecursiveComparison()
        .ignoringFields("subjects", "id").isEqualTo(request);
    assertThat(courseStored).usingRecursiveComparison()
        .ignoringFields("subjects", "id", "createdDate", "updatedDate")
        .withEqualsForFields(dateComparator, "startDate", "endDate")
        .isEqualTo(request);
  }

  @Test
  public void shouldReturnConflictWhenCourseExist() {
    var request = CourseRequestMother.buildValidRequest();

    courseRepository.save(entityMapper.courseToEntity(mapper.courseRequestToCourse(request)));
    var response = restTemplate.postForEntity(COURSE_URL, request, ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }

  @Test
  public void shouldReturnBadRequestWhenNameIsEmpty() {
    var request = CourseRequestMother.buildInvalidRequestByName();

    var response = restTemplate.postForEntity(COURSE_URL, request, ErrorResponse.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    assertThat(response.getBody().getMessage()).isEqualTo("Bad request the following fields have errors");
    assertThat(response.getBody().getFieldsError()).containsExactly("Name cannot be empty");
  }
}
