package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;

import java.util.stream.Collectors;

public class SubjectRequestMother {

  public static SubjectRequest buildSubjectRequest(Subject subject) {
    return new SubjectRequest(
        subject.getName(),
        subject.getDescription(),
        subject.getProfessor().getUsername(),
        subject.getCourse().getId(),
        subject.getSchedules().stream().map(ScheduleRequestMother::buildFrom).collect(Collectors.toList()));
  }
}
