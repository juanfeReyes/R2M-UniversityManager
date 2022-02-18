package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GetCoursesTest {

  private GetCourses getCourses;

  @Mock
  private CourseRepository courseRepository;

  @BeforeEach
  public void init() {
    Mockito.clearInvocations(courseRepository);
    getCourses = new GetCourses(courseRepository);
  }

  @Test
  public void shouldGetCourses() {
    var expectedCourse = CourseMother.validCourse();
    var page = new PageImpl<CourseEntity>(List.of(CourseEntity.toEntity(expectedCourse)));
    when(courseRepository.findAll(any(Pageable.class))).thenReturn(page);

    var pageable = PageRequest.of(1, 5);
    var actualCourses = getCourses.execute(pageable);

    assertThat(actualCourses.getContent()).first()
        .usingRecursiveComparison().isEqualTo(expectedCourse);
  }
}
