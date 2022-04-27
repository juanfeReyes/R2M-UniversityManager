package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IEnrollStudentToSubject;
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
public class EnrollStudentToSubjectController {

  private final IEnrollStudentToSubject assignStudentToSubject;

  @Autowired
  public EnrollStudentToSubjectController(IEnrollStudentToSubject assignStudentToSubject) {
    this.assignStudentToSubject = assignStudentToSubject;
  }

  @Operation(summary = "assign student to subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PutMapping(value = "/{username}/subject/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void enrollStudentToSubject(@PathVariable String username, @PathVariable String subjectId) throws Exception {
    assignStudentToSubject.execute(subjectId, username);
  }
}
