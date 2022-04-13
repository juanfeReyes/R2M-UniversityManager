package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceConflictException;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteCourse implements IDeleteCourse{

  private final CourseRepository courseRepository;

  private final SubjectRepository subjectRepository;

  @Autowired
  public DeleteCourse(CourseRepository courseRepository, SubjectRepository subjectRepository) {
    this.courseRepository = courseRepository;
    this.subjectRepository = subjectRepository;
  }

  public void execute(String courseId) {
    if(!courseRepository.existsById(courseId)){
      throw new ResourceNotFoundException(String.format("Course with id: %s does not exists", courseId));
    }

    var courseSubjects = subjectRepository.findAllByCourse(courseId);
    if(courseSubjects.size() > 0){
      throw new ResourceConflictException(String.format("Course has %s subjects, must have none", courseSubjects.size()));
    }

    courseRepository.deleteById(courseId);
  }
}
