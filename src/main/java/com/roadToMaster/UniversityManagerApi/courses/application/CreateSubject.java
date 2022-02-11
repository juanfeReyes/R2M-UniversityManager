package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateSubject implements ICreateSubject {

  private final CourseRepository courseRepository;

  private final SubjectRepository subjectRepository;

  @Autowired
  public CreateSubject(CourseRepository courseRepository, SubjectRepository subjectRepository) {
    this.courseRepository = courseRepository;
    this.subjectRepository = subjectRepository;
  }

  public Subject execute(String id, String name, String description, String courseName) {
    if (subjectRepository.existsById(id)) {
      throw new ResourceAlreadyCreatedException(String.format("Subject already exists with id: %s", id));
    }

    var courseEntity = courseRepository.findByName(courseName);
    if (courseEntity.isEmpty()) {
      throw new ResourceNotFoundException(String.format("Course with name %s does not exists", courseName));
    }

    var subject = new Subject(id, name, description, CourseEntity.toDomain(courseEntity.get()));
    subjectRepository.save(SubjectEntity.toEntity(subject, courseEntity.get()));

    return subject;
  }
}
