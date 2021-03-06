package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IDeleteCourse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@Validated
@SecurityRequirement(name = "basicAuth")
public class DeleteCourseController {

  private final IDeleteCourse deleteCourse;

  @Autowired
  public DeleteCourseController(IDeleteCourse deleteCourse) {
    this.deleteCourse = deleteCourse;
  }

  @Operation(summary = "Delete course by Id", security = {@SecurityRequirement(name = "OAuthScheme")})
  @DeleteMapping(value = "/{courseId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteCourse(@NotBlank(message = "courseId must not be blank") @PathVariable String courseId) {
    deleteCourse.execute(courseId);
  }

}
