package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCourse implements ICreateCourse{

  private CourseRepository courseRepository;

  @Autowired
  public CreateCourse(CourseRepository courseRepository){
    this.courseRepository = courseRepository;
  }

  public Course execute(Course course){
    if(courseRepository.findByName(course.getName()).isPresent()){
      throw new ResourceAlreadyCreatedException(String.format("Course with name: %s already exists", course.getName()));
    }

    courseRepository.save(CourseEntity.toEntity(course));

    return course;
  }
}
