package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.IUpdateSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.CoursesMapper;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.stream.Collectors;

@RestController
@RequestMapping("subject")
@Tag(name = "Subject")
@SecurityRequirement(name = "basicAuth")
public class UpdateSubjectController {

  private final IUpdateSubject updateSubject;

  private final CoursesMapper coursesMapper;

  @Autowired
  public UpdateSubjectController(IUpdateSubject updateSubject, CoursesMapper coursesMapper) {
    this.updateSubject = updateSubject;
    this.coursesMapper = coursesMapper;
  }

  @Operation(summary = "Update a subject", security = {@SecurityRequirement(name = "OAuthScheme")})
  @PutMapping(value = "/{subjectId}",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.OK)
  public SubjectResponse updateSubject(@PathVariable String subjectId,
          @Valid @RequestBody SubjectRequest subjectRequest){
    var schedules = subjectRequest.getSchedules().stream().map(coursesMapper::scheduleRequestToSchedule)
        .collect(Collectors.toList());
    var updatedSubject = updateSubject.execute(subjectId, subjectRequest.getName(), subjectRequest.getDescription(),
        subjectRequest.getCourseId(), subjectRequest.getProfessorUserName(), schedules);
    return coursesMapper.subjectToResponse(updatedSubject);
  }
}
