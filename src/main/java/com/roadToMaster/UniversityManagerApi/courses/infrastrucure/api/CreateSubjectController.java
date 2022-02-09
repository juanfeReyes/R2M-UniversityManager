package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api;

import com.roadToMaster.UniversityManagerApi.courses.application.ICreateSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@Validated
@RequestMapping("course")
@Tag(name = "Course")
public class CreateSubjectController {

  private final ICreateSubject createSubject;

  @Autowired
  public CreateSubjectController(ICreateSubject createSubject) {
    this.createSubject = createSubject;
  }

  @PostMapping(value = "/{courseId}/subject",produces = MediaType.APPLICATION_JSON_VALUE)
  @ResponseStatus(HttpStatus.CREATED)
  public void createSubject(@PathVariable String courseId,
                            @Valid @RequestBody SubjectRequest subjectRequest) {

    createSubject.execute(subjectRequest.getId(), subjectRequest.getName(),
        subjectRequest.getDescription(), courseId);
  }
}
