package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapperImpl;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
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
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateCourseTest {

  private final CoursesEntityMapper entityMapper = new CoursesEntityMapperImpl();

  @Mock
  private CourseRepository courseRepositoryMock;

  private CreateCourse createCourse;

  @Captor
  private ArgumentCaptor<CourseEntity> courseEntityCaptor;

  @BeforeEach
  public void init() {
    Mockito.clearInvocations(courseRepositoryMock);
    this.createCourse = new CreateCourse(entityMapper, courseRepositoryMock);
  }

  @Test
  public void ShouldSaveCourse() {
    var course = CourseMother.validCourse();
    when(courseRepositoryMock.findByName(anyString())).thenReturn(Optional.empty());
    when(courseRepositoryMock.save(any())).thenReturn(entityMapper.courseToEntity(course));

    var result = createCourse.execute(course);

    verify(courseRepositoryMock, times(1)).save(courseEntityCaptor.capture());
    assertThat(courseEntityCaptor.getValue()).usingRecursiveComparison()
        .ignoringFields("createdDate", "updatedDate")
        .isEqualTo(course);
    assertThat(result).usingRecursiveComparison().isEqualTo(course);
  }

  @Test
  public void ShouldThrowExceptionWhenCourseExists() {
    var course = CourseMother.validCourse();
    when(courseRepositoryMock.findByName(anyString())).thenReturn(Optional.of(entityMapper.courseToEntity(course)));

    assertThatThrownBy(() -> {
      createCourse.execute(course);
    }).isInstanceOf(ResourceAlreadyCreatedException.class)
        .hasMessage(String.format("Course with name: %s already exists", course.getName()));
    verify(courseRepositoryMock, never()).save(courseEntityCaptor.capture());
  }
}
