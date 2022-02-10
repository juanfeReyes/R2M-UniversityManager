package com.roadToMaster.UniversityManagerApi.component.course;

import com.roadToMaster.UniversityManagerApi.component.ComponentTestBase;
import com.roadToMaster.UniversityManagerApi.courses.domain.CourseMother;
import com.roadToMaster.UniversityManagerApi.courses.domain.SubjectMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastructure.SubjectRequestMother;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;

public class CreateSubjectTest extends ComponentTestBase {

  private static final String SUBJECT_URL = "/course/{courseName}/subject";

  @Autowired
  private SubjectRepository subjectRepository;

  @Autowired
  private CourseRepository courseRepository;

  @BeforeEach
  public void init(){
    subjectRepository.deleteAll();
    courseRepository.deleteAll();
    courseRepository.save(CourseEntity.toEntity(CourseMother.validCourse()));
  }

  @Test
  public void ShouldCreateSubject(){
    var expectedSubject = SubjectMother.validSubject(CourseMother.validCourse());

    var request = SubjectRequestMother.buildSubjectRequest(expectedSubject);

    var response = restTemplate.exchange(SUBJECT_URL, HttpMethod.POST, new HttpEntity<>(request),
        Void.class, expectedSubject.getCourse().getName());

    var subjectSaved = subjectRepository.findById(expectedSubject.getId());

    assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    assertThat(subjectSaved).isPresent();
    assertThat(subjectSaved.get()).usingRecursiveComparison()
        .ignoringFields("course").isEqualTo(expectedSubject);
  }
}
