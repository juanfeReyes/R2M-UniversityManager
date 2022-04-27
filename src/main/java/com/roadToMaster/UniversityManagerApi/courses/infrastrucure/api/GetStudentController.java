package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IGetStudent;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.StudentResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("student")
@Tag(name = "Student")
@Validated
@SecurityRequirement(name = "basicAuth")
public class GetStudentController {

  private final IGetStudent getStudent;

  private final CoursesMapper coursesMapper;

  public GetStudentController(IGetStudent getStudent, CoursesMapper coursesMapper) {
    this.getStudent = getStudent;
    this.coursesMapper = coursesMapper;
  }

  @Operation(summary = "Get subjects", security = {@SecurityRequirement(name = "OAuthScheme")})
  @GetMapping(value = "/{studentUsername}", produces = MediaType.APPLICATION_JSON_VALUE)
  public StudentResponse getSubjects(@PathVariable String studentUsername) {
    var result = getStudent.execute(studentUsername);
    var subjectsResponse = result.getSubjects().stream().map(coursesMapper::subjectToResponse).collect(Collectors.toList());
    return coursesMapper.studentToResponse(result, subjectsResponse);
  }
}
