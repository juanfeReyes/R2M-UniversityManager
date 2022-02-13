package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.ICreateCourse;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@SecurityRequirement(name = "basicAuth")
public class CreateCourseController {

  private final ICreateCourse createCourse;

  @Autowired
  public CreateCourseController(ICreateCourse createCourse) {
    this.createCourse = createCourse;
  }

  @Operation(summary = "Create Course", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('DIRECTOR')")
  public Course createCourse(@Valid @RequestBody CourseRequest courseRequestDTO) throws Exception {
    return createCourse.execute(CourseRequest.toDomain(courseRequestDTO));
  }
}
