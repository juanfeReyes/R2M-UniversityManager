package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateSubjectTest {

  private CreateSubject createSubject;

  @Mock
  private CourseRepository courseRepositoryMock;

  @Mock
  private SubjectRepository subjectRepository;

  @Captor
  private ArgumentCaptor<SubjectEntity> subjectArgumentCaptor;

  @BeforeEach
  public void setup(){
    Mockito.clearInvocations(courseRepositoryMock, subjectRepository);
    createSubject = new CreateSubject(courseRepositoryMock, subjectRepository);
  }

  @Test
  public void ShouldCreateSubject(){
    var subject = SubjectMother.validSubject(CourseMother.validCourse());
    var courseEntity = CourseEntity.toEntity(subject.getCourse());
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(subject.getCourse().getName()))).thenReturn(Optional.of(courseEntity));

    createSubject.execute(subject.getId(), "modernism", "moder subject", subject.getCourse().getName());


    verify(subjectRepository, times(1)).save(subjectArgumentCaptor.capture());
    assertThat(subjectArgumentCaptor.getValue()).usingRecursiveComparison().isEqualTo(subject);
  }

  @Test
  public void ShouldThrowResourceAlreadyExists(){
    var subject = SubjectMother.validSubject(CourseMother.validCourse());
    var courseEntity = CourseEntity.toEntity(subject.getCourse());
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.TRUE);

    assertThatThrownBy(() ->
        createSubject.execute(subject.getId(), "modernism", "moder subject", subject.getCourse().getName()))
        .isInstanceOf(ResourceAlreadyCreatedException.class)
            .hasMessage(String.format("Subject already exists with id: %s", subject.getId()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowResourceNotFound(){
    var subject = SubjectMother.validSubject(CourseMother.validCourse());
    var courseEntity = CourseEntity.toEntity(subject.getCourse());
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(subject.getCourse().getName()))).thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        createSubject.execute(subject.getId(), "modernism", "moder subject", subject.getCourse().getName()))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("Course with name %s does not exists", subject.getCourse().getName()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }
}
