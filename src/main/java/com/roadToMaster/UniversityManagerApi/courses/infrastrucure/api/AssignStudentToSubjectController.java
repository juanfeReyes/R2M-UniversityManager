package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IAssignStudentToSubject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
@Tag(name = "Student")
@SecurityRequirement(name = "basicAuth")
public class AssignStudentToSubjectController {

  private final IAssignStudentToSubject assignStudentToSubject;

  @Autowired
  public AssignStudentToSubjectController(IAssignStudentToSubject assignStudentToSubject) {
    this.assignStudentToSubject = assignStudentToSubject;
  }

  @Operation(summary = "assign student to subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PutMapping(value = "/{studentId}/subject/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void assignStudentToSubject(@PathVariable String studentId, @PathVariable String subjectId) throws Exception {
    assignStudentToSubject.execute(subjectId, studentId);
  }
}
