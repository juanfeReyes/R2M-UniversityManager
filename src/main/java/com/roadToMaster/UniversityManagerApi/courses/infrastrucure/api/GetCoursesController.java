package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IGetCourses;
import com.roadToMaster.UniversityManagerApi.courses.domain.Course;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("course")
@Tag(name = "Course")
@Validated
public class GetCoursesController {

  private final IGetCourses getCourses;

  @Autowired
  public GetCoursesController(IGetCourses getCourses) {
    this.getCourses = getCourses;
  }

  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResponse<Course> getCourses( @RequestParam @Min(value = 0, message = "page number must be more at least 0") int pageNumber,
                                          @RequestParam @Min(value = 1, message = "page size must be more at least 1") int pageSize){
    var page = PageRequest.of(pageNumber, pageSize);
    var coursesPage =  getCourses.execute(page);
    return new PageResponse<Course>(coursesPage.getTotalElements(), coursesPage.getNumber(), coursesPage.getContent());
  }
}
