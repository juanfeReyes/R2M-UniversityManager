package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IGetStudent;
import com.roadToMaster.UniversityManagerApi.courses.domain.Student;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectResponse;
import com.roadToMaster.UniversityManagerApi.shared.infrastructure.api.dto.PageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.Min;

@RestController
@RequestMapping("student")
@Tag(name = "Student")
@Validated
@SecurityRequirement(name = "basicAuth")
public class GetStudentController {

  private final IGetStudent getStudent;

  public GetStudentController(IGetStudent getStudent) {
    this.getStudent = getStudent;
  }

  @Operation(summary = "Get subjects", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "/{studentUsername}", produces = MediaType.APPLICATION_JSON_VALUE)
  public Student getSubjects(@PathVariable String studentUsername) {
    //TODO: create student response to remove students, professor and course
    return getStudent.execute(studentUsername);
  }
}
