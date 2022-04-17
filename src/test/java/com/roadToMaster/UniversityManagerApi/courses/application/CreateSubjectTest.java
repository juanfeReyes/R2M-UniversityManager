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

    when(subjectRepository.findByNameAndCourse(anyString(), anyString())).thenReturn(Optional.empty());
    when(subjectRepository.save(any())).thenReturn(entityMapper.subjectToEntity(subject, courseEntity));
    when(courseRepositoryMock.findById(ArgumentMatchers.eq(course.getName()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(userEntityMapper.userToEntity(professor)));

    createSubject.execute(subject.getName(), subject.getDescription(),
        course.getName(), professor.getUsername(), schedules);


    verify(subjectRepository, times(1)).save(subjectArgumentCaptor.capture());
    assertThat(subjectArgumentCaptor.getValue()).usingRecursiveComparison()
        .ignoringFields("schedules", "professor", "course", "createdDate", "active", "updatedDate", "id")
        .isEqualTo(subject);
  }

  @Test
  public void ShouldThrowResourceAlreadyExists() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));
    var subject = SubjectMother.validSubject(professor, schedules, course);

    var courseEntity = entityMapper.courseToEntity(course);
    when(courseRepositoryMock.findById(anyString())).thenReturn(Optional.of(courseEntity));
    when(subjectRepository.findByNameAndCourse(anyString(), anyString())).thenReturn(Optional.of(entityMapper.subjectToEntity(subject, courseEntity)));

    assertThatThrownBy(() ->
        createSubject.execute( "modernism", "moder subject",
            course.getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceAlreadyCreatedException.class)
        .hasMessage(String.format("Subject within the course: %s and with name: %s already exists", course.getName(), "modernism"));
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }

  @Test
  public void ShouldThrowResourceNotFound() {
    var course = CourseMother.validCourse();
    var professor = UserMother.buildValid();
    var schedules = List.of(ScheduleMother.buildSchedule(0, 10));

    assertThatThrownBy(() ->
        createSubject.execute("modernism", "moder subject",
            course.getName(), professor.getUsername(), schedules))
        .isInstanceOf(ResourceNotFoundException.class)
        .hasMessage(String.format("Course with id %s does not exists", course.getName()));
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
    when(subjectRepository.findByNameAndCourse(anyString(), anyString())).thenReturn(Optional.empty());
    when(courseRepositoryMock.findById(ArgumentMatchers.eq(course.getId()))).thenReturn(Optional.of(courseEntity));
    when(userRepository.findByUsername(eq(professor.getUsername()))).thenReturn(Optional.of(userEntityMapper.userToEntity(professor)));

    assertThatThrownBy(() ->
        createSubject.execute("modernism", "moder subject",
            course.getId(), professor.getUsername(), schedules))
        .isInstanceOf(ScheduleConflictException.class)
        .hasMessage("Cannot create subject schedules overlap with professors schedules");
    verify(subjectRepository, never()).save(subjectArgumentCaptor.capture());
  }
}
