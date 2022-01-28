package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CreateCourse {

  private CourseRepository courseRepository;

  @Autowired
  public CreateCourse(CourseRepository courseRepository){
    this.courseRepository = courseRepository;
  }

  public Course execute(Course course){
    //TODO: Validate by name
    if(courseRepository.existsById(course.getId())){
      // throw exception that course already exists
    }

    // save course
    courseRepository.save(CourseEntity.toEntity(course));

    return course;
  }
}
