package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.ICreateCourse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequest;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@SecurityRequirement(name = "basicAuth")
@Validated
public class CreateCourseController {

  private final ICreateCourse createCourse;

  private final CoursesMapper mapper;

  @Autowired
  public CreateCourseController(ICreateCourse createCourse, CoursesMapper mapper) {
    this.createCourse = createCourse;
    this.mapper = mapper;
  }

  @Operation(summary = "Create Course", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public CourseResponse createCourse(@Valid @RequestBody CourseRequest courseRequestDTO) throws Exception {
    var courses = mapper.courseRequestToCourse(courseRequestDTO);
    var createdCourse = createCourse.execute(courses);
    return mapper.courseToResponse(createdCourse);
  }
}
