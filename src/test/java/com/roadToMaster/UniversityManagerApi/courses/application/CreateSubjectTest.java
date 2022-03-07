package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateSubjectTest {

  private CreateSubject createSubject;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CourseRepository courseRepositoryMock;

  @Mock
  private SubjectRepository subjectRepository;

  @Captor
  private ArgumentCaptor<SubjectEntity> subjectArgumentCaptor;

  @BeforeEach
  public void setup() {
    Mockito.clearInvocations(courseRepositoryMock, subjectRepository);
    createSubject = new CreateSubject(userRepository, courseRepositoryMock, subjectRepository);
  }

  @Test
  public void ShouldCreateSubject() {
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(CourseMother.validCourse(), professor, schedules);
    var courseEntity = CourseEntity.toEntity(subject.getCourse());

    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(subject.getCourse().getName()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(UserEntity.toEntity(professor)));

    createSubject.execute(subject.getId(), "modernism", "moder subject",
        subject.getCourse().getName(), professor.getUsername(), schedules);


    verify(subjectRepository, times(1)).save(subjectArgumentCaptor.capture());
    assertThat(subjectArgumentCaptor.getValue()).usingRecursiveComparison()
        .ignoringFields("schedules", "professor")
        .isEqualTo(subject);
  }

  @Test
  public void ShouldThrowResourceAlreadyExists() {
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(CourseMother.validCourse(), professor, schedules);
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.TRUE);

    assertThatThrownBy(() ->
        createSubject.execute(subject.getId(), "modernism", "moder subject",
            subject.getCourse().getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceAlreadyCreatedException.class)
        .hasMessage(String.format("Subject already exists with id: %s", subject.getId()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowResourceNotFound() {
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(CourseMother.validCourse(), professor, schedules);
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(subject.getCourse().getName()))).thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        createSubject.execute(subject.getId(), "modernism", "moder subject",
            subject.getCourse().getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("Course with name %s does not exists", subject.getCourse().getName()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowOverlappedSchedulesException() {
    var professor = UserMother.buildValid();
    var overlappedSchedules = List.of(ScheduleMother.buildSchedule(5, 5));
    var existingSubject = SubjectMother.validSubject(CourseMother.validCourse(), professor, overlappedSchedules);

    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(CourseMother.validCourse(), professor, schedules);
    var courseEntity = CourseEntity.toEntity(subject.getCourse());

    var existingSubjectEntity = SubjectEntity.toEntity(existingSubject, courseEntity);
    when(subjectRepository.findByProfessorUsername(ArgumentMatchers.eq(professor.getUsername()))).thenReturn(List.of(existingSubjectEntity));
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(subject.getCourse().getName()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(UserEntity.toEntity(professor)));

    assertThatThrownBy(() ->
        createSubject.execute(subject.getId(), "modernism", "moder subject",
            subject.getCourse().getName(), professor.getUsername(), schedules))
        .isInstanceOf(ScheduleConflictException.class)
        .hasMessage("Cannot create subject the following schedules overlap with professors schedules");
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }
}
