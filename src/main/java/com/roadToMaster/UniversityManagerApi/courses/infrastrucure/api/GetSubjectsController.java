package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IGetSubjects;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectResponse;
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
@RequestMapping("subject")
@Tag(name = "Subject")
@Validated
@SecurityRequirement(name = "basicAuth")
public class GetSubjectsController {

  private final IGetSubjects getSubjects;

  private final CoursesMapper coursesMapper;

  @Autowired
  public GetSubjectsController(IGetSubjects getSubjects, CoursesMapper coursesMapper) {
    this.getSubjects = getSubjects;
    this.coursesMapper = coursesMapper;
  }

  @Operation(summary = "Get subjects", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  public PageResponse<SubjectResponse> getSubjects(@RequestParam @Min(value = 0, message = "page number must be more at least 0") int pageNumber,
                                                   @RequestParam @Min(value = 1, message = "page size must be more at least 1") int pageSize) {
    var page = PageRequest.of(pageNumber, pageSize);
    var subjectsPage = getSubjects.execute(page);
    return new PageResponse(subjectsPage.getTotalElements(), subjectsPage.getNumber(), subjectsPage.map(coursesMapper::subjectToResponse).getContent());
  }
}
