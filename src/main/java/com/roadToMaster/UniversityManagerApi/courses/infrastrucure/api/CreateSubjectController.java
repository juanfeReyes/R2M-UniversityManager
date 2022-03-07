package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.ICreateSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.ScheduleRequest;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("course")
@Tag(name = "Course")
@SecurityRequirement(name = "basicAuth")
public class CreateSubjectController {

  private final ICreateSubject createSubject;

  @Autowired
  public CreateSubjectController(ICreateSubject createSubject) {
    this.createSubject = createSubject;
  }

  @Operation(summary = "Create Subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(value = "/{courseId}/subject", produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  @PreAuthorize("hasRole('PROFESSOR')")
  public void createSubject(@PathVariable String courseId,
                            @Valid @RequestBody SubjectRequest subjectRequest) {
    var schedules = subjectRequest.getSchedules().stream().map(ScheduleRequest::toDomain)
        .collect(Collectors.toList());
    createSubject.execute(subjectRequest.getId(), subjectRequest.getName(),
        subjectRequest.getDescription(), courseId, subjectRequest.getProfessorUserName(), schedules);
  }
}
