package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectCreationRequest;

import java.util.stream.Collectors;

public class SubjectCreationMother {

  public static SubjectCreationRequest buildSubjectRequest(Subject subject) {
    return new SubjectCreationRequest(
        subject.getName(),
        subject.getDescription(),
        subject.getProfessor().getUsername(),
        subject.getCourse().getId(),
        subject.getSchedules().stream().map(ScheduleRequestMother::buildFrom).collect(Collectors.toList()));
  }

}
