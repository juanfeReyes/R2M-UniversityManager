package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceAlreadyCreatedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;

@Service
@Validated
public class CreateCourse implements ICreateCourse {

  private final CoursesEntityMapper entityMapper;

  private final CourseRepository courseRepository;

  @Autowired
  public CreateCourse(CoursesEntityMapper entityMapper, CourseRepository courseRepository) {
    this.entityMapper = entityMapper;
    this.courseRepository = courseRepository;
  }

  public Course execute(@Valid Course course) {
    if (courseRepository.findByName(course.getName()).isPresent()) {
      throw new ResourceAlreadyCreatedException(String.format("Course with name: %s already exists", course.getName()));
    }

    courseRepository.save(entityMapper.courseToEntity(course));

    return course;
  }
}
