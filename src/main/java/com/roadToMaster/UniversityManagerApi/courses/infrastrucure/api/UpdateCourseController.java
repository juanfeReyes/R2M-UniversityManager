package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IUpdateCourse;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@SecurityRequirement(name = "basicAuth")
public class UpdateCourseController {

  private final IUpdateCourse updateCourse;

  private final CoursesMapper coursesMapper;

  public UpdateCourseController(IUpdateCourse updateCourse, CoursesMapper coursesMapper) {
    this.updateCourse = updateCourse;
    this.coursesMapper = coursesMapper;
  }

  @Operation(summary = "Update course", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PutMapping(value = "/{courseId}",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public Course updateCourse(@PathVariable String courseId,
      @Valid @RequestBody CourseRequest courseRequestDTO){

    var response = updateCourse.execute(coursesMapper.courseRequestToCourse(courseRequestDTO, courseId));
    return response;
  }
}
