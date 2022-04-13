package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IGetCourses;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CourseResponse;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@Validated
@SecurityRequirement(name = "basicAuth")
public class GetCoursesController {

  private final IGetCourses getCourses;

  private final CoursesMapper coursesMapper;

  @Autowired
  public GetCoursesController(IGetCourses getCourses, CoursesMapper coursesMapper) {
    this.getCourses = getCourses;
    this.coursesMapper = coursesMapper;
  }

  @Operation(summary = "Get courses", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResponse<CourseResponse> getCourses(@RequestParam @Min(value = 0, message = "page number must be more at least 0") int pageNumber,
                                         @RequestParam @Min(value = 1, message = "page size must be more at least 1") int pageSize) {
    var page = PageRequest.of(pageNumber, pageSize);
    var coursesPage = getCourses.execute(page).map(coursesMapper::courseToResponse);
    return new PageResponse(coursesPage.getTotalElements(), coursesPage.getNumber(), coursesPage.getContent());
  }
}
