package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.CourseRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequestDTO;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
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

  @BeforeEach
  public void init() {
    courseRepository.deleteAll();
  }

  @Test
  public void shouldCreateCourse() {
    var request = CourseRequestMother.buildValidRequest();
    var response = restTemplate.postForEntity(COURSE_URL, request, Course.class);

    var courseStored = courseRepository.findByName(request.getName()).get();

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    assertThat(response.getBody()).usingRecursiveComparison().isEqualTo(request);
    assertThat(courseStored).usingRecursiveComparison()
        .withEqualsForFields(dateComparator, "startDate", "endDate")
        .isEqualTo(request);
  }

  @Test
  public void shouldReturnConflictWhenCourseExist() {
    var request = CourseRequestMother.buildValidRequest();

    courseRepository.save(CourseEntity.toEntity(CourseRequestDTO.toDomain(request)));
    var response = restTemplate.postForEntity(COURSE_URL, request, Course.class);

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
  }
}
