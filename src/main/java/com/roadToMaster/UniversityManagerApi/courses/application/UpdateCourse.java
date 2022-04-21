package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IUpdateCourse;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UpdateCourse implements IUpdateCourse {

  private final CourseRepository courseRepository;

  private final CoursesEntityMapper courseMapper;

  public UpdateCourse(CourseRepository courseRepository, CoursesEntityMapper courseMapper) {
    this.courseRepository = courseRepository;
    this.courseMapper = courseMapper;
  }

  public Course execute(Course course) {

    //Validate course already exists then throw exception
    if (courseRepository.findById(course.getId()).isEmpty()) {
      throw new ResourceNotFoundException("Course does not exists");
    }

    // Update course
    var updatedCourse = courseRepository.save(courseMapper.courseToEntity(course));
    return courseMapper.courseToDomain(updatedCourse, null);
  }
}
