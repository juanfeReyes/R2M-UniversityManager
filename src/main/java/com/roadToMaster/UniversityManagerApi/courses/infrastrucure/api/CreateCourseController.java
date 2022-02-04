package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.ICreateCourse;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequestDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
public class CreateCourseController {

  private ICreateCourse createCourse;

  @Autowired
  public CreateCourseController(ICreateCourse createCourse){
    this.createCourse = createCourse;
  }

  @Validated
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public Course createCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) throws Exception {
    return createCourse.execute(CourseRequestDTO.toDomain(courseRequestDTO));
  }
}
