package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class GetCourses implements IGetCourses{

  private final CourseRepository courseRepository;

  @Autowired
  public GetCourses(CourseRepository courseRepository) {
    this.courseRepository = courseRepository;
  }

  //TODO: should i build the pageable in the use case? what happend for invalid input??
  @Override
  public Page<Course> execute(Pageable pageable) {

    var courses = courseRepository.findAll(pageable);

    //TODO: Cannot return page object must create pageable response
    return courses.map(CourseEntity::toDomain);
  }
}
