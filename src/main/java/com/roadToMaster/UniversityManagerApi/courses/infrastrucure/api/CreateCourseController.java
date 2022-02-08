package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.ICreateCourse;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
public class CreateCourseController {

  private final ICreateCourse createCourse;

  @Autowired
  public CreateCourseController(ICreateCourse createCourse) {
    this.createCourse = createCourse;
  }

  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Course createCourse(@Valid @RequestBody CourseRequest courseRequestDTO) throws Exception {
    return createCourse.execute(CourseRequest.toDomain(courseRequestDTO));
  }
}
