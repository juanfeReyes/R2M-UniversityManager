package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IGetCourses;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.CourseRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GetCourses implements IGetCourses {

  private final CoursesEntityMapper entityMapper;

  private final CourseRepository courseRepository;

  @Autowired
  public GetCourses(CoursesEntityMapper entityMapper, CourseRepository courseRepository) {
    this.entityMapper = entityMapper;
    this.courseRepository = courseRepository;
  }

  @Override
  public Page<Course> execute(Pageable pageable) {

    var courses = courseRepository.findAll(pageable);

    return courses.map(c -> entityMapper.courseToDomain(c, List.of()));
  }
}
