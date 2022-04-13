package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.ScheduleMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.UserMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.exceptions.ScheduleConflictException;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.ScheduleRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapperImpl;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapperImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CreateSubjectTest {

  private final CoursesEntityMapper entityMapper = new CoursesEntityMapperImpl();
  private final UserEntityMapper userEntityMapper = new UserEntityMapperImpl();

  private CreateSubject createSubject;

  @Mock
  private UserRepository userRepository;

  @Mock
  private CourseRepository courseRepositoryMock;

  @Mock
  private SubjectRepository subjectRepository;

  @Mock
  private ScheduleRepository scheduleRepository;

  @Captor
  private ArgumentCaptor<SubjectEntity> subjectArgumentCaptor;

  @BeforeEach
  public void setup() {
    Mockito.clearInvocations(courseRepositoryMock, subjectRepository);
    createSubject = new CreateSubject(entityMapper, userEntityMapper, userRepository, courseRepositoryMock, subjectRepository, scheduleRepository);
  }

  @Test
  public void ShouldCreateSubject() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(professor, schedules, course);

    var courseEntity = entityMapper.courseToEntity(course);

    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(course.getName()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(userEntityMapper.userToEntity(professor)));

    createSubject.execute(subject.getName(), subject.getDescription(),
        course.getName(), professor.getUsername(), schedules);


    verify(subjectRepository, times(1)).save(subjectArgumentCaptor.capture());
    assertThat(subjectArgumentCaptor.getValue()).usingRecursiveComparison()
        .ignoringFields("schedules", "professor", "course")
        .isEqualTo(subject);
  }

  @Test
  public void ShouldThrowResourceAlreadyExists() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(professor, schedules, course);

    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.TRUE);

    assertThatThrownBy(() ->
        createSubject.execute( "modernism", "moder subject",
            course.getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceAlreadyCreatedException.class)
        .hasMessage(String.format("Subject already exists with id: %s", subject.getId()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowResourceNotFound() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(professor, schedules, course);
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(course.getName()))).thenReturn(Optional.empty());

    assertThatThrownBy(() ->
        createSubject.execute("modernism", "moder subject",
            course.getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("Course with name %s does not exists", course.getName()));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowOverlappedSchedulesException() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var overlappedSchedules = List.of(ScheduleMother.buildSchedule(5, 5));
    var existingSubject = SubjectMother.validSubject(professor, overlappedSchedules, course);

    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(professor, schedules, course);
    var courseEntity = entityMapper.courseToEntity(course);

    var existingSubjectEntity = entityMapper.subjectToEntity(existingSubject, courseEntity);
    when(scheduleRepository.findBySubjectId(anyList())).thenReturn(overlappedSchedules.stream().map(s -> entityMapper.scheduleToEntity(s, existingSubjectEntity)).collect(Collectors.toList()));
    when(subjectRepository.findByProfessorUsername(ArgumentMatchers.eq(professor.getUsername()))).thenReturn(List.of(existingSubjectEntity));
    when(subjectRepository.existsById(ArgumentMatchers.eq(subject.getId()))).thenReturn(Boolean.FALSE);
    when(courseRepositoryMock.findByName(ArgumentMatchers.eq(course.getName()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(userEntityMapper.userToEntity(professor)));

    assertThatThrownBy(() ->
        createSubject.execute("modernism", "moder subject",
            course.getName(), professor.getUsername(), schedules))
        .isInstanceOf(ScheduleConflictException.class)
        .hasMessage("Cannot create subject schedules overlap with professors schedules");
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }
}
