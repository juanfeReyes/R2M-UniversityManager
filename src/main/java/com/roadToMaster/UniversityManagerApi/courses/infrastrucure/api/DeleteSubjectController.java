package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IDeleteSubject;
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
@RequestMapping("subject")
@Tag(name = "Subject")
@Validated
@SecurityRequirement(name = "basicAuth")
public class DeleteSubjectController {

  private final IDeleteSubject deleteSubject;

  @Autowired
  public DeleteSubjectController(IDeleteSubject deleteSubject) {
    this.deleteSubject = deleteSubject;
  }

  @Operation(summary = "Delete subject by Id", security = {@SecurityRequirement(name = "OAuthScheme")})
  @DeleteMapping(value = "/{subjectId}", produces = MediaType.APPLICATION_JSON_VALUE)
  public void deleteCourse(@NotBlank(message = "Id should not be empty") @PathVariable String subjectId){
    deleteSubject.execute(subjectId);
  }
}
