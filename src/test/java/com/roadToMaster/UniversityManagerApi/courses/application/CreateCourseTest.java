package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCourseTest {

  @Mock
  private CourseRepository courseRepositoryMock;

  private CreateCourse createCourse;

  @Captor
  private ArgumentCaptor<CourseEntity> courseEntityCaptor;

  @BeforeEach
  public void init(){
    Mockito.clearInvocations(courseRepositoryMock);
    this.createCourse = new CreateCourse(courseRepositoryMock);
  }

  @Test
  public void ShouldSaveCourse() throws Exception {
    var course = CourseMother.validCourse();
    when(courseRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());

    createCourse.execute(course);

    verify(courseRepositoryMock, times(1)).save(courseEntityCaptor.capture());
    assertThat(courseEntityCaptor.getValue())
        .usingRecursiveComparison()
        .isEqualTo(course);

  }
}