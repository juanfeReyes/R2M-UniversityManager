package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.ICreateSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectCreationRequest;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@Validated
@RequestMapping("subject")
@Tag(name = "Subject")
@SecurityRequirement(name = "basicAuth")
public class CreateSubjectController {

  private final ICreateSubject createSubject;

  private final CoursesMapper mapper;

  @Autowired
  public CreateSubjectController(ICreateSubject createSubject, CoursesMapper mapper) {
    this.createSubject = createSubject;
    this.mapper = mapper;
  }

  @Operation(summary = "Create Subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public SubjectResponse createSubject(@Valid @RequestBody SubjectCreationRequest subjectRequest) {
    var schedules = subjectRequest.getSchedules().stream().map(mapper::scheduleRequestToSchedule)
        .collect(Collectors.toList());
    var createdSubject = createSubject.execute(subjectRequest.getName(),
        subjectRequest.getDescription(), subjectRequest.getCourseId(), subjectRequest.getProfessorUserName(), schedules);

    return mapper.subjectToResponse(createdSubject);
  }
}
