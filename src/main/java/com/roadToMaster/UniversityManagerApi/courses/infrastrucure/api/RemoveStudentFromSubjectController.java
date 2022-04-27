package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IRemoveStudentFromSubject;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("student")
@Tag(name = "Student")
@SecurityRequirement(name = "basicAuth")
public class RemoveStudentFromSubjectController {

  private final IRemoveStudentFromSubject removeStudentFromSubject;

  public RemoveStudentFromSubjectController(IRemoveStudentFromSubject removeStudentFromSubject) {
    this.removeStudentFromSubject = removeStudentFromSubject;
  }

  @Operation(summary = "remove student to subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @DeleteMapping(value = "/{username}/subject/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public void removeStudentFromSubject(@PathVariable String username, @PathVariable String subjectId) throws Exception {
    removeStudentFromSubject.execute(subjectId, username);
  }
}
