package com.roadToMaster.UniversityManagerApi.courses.infrastructure;

import com.roadToMaster.UniversityManagerApi.FakerUtil;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.api.dto.SubjectRequest;

import java.util.UUID;
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

  public static SubjectRequest buildSubjectRequestWithRandomCourseId(Subject subject) {
    return new SubjectRequest(
        subject.getName(),
        subject.getDescription(),
        subject.getProfessor().getUsername(),
        UUID.randomUUID().toString(),
        subject.getSchedules().stream().map(ScheduleRequestMother::buildFrom).collect(Collectors.toList()));
  }

  public static SubjectRequest buildSubjectRequestWithRandomProfessorUsername(Subject subject) {
    var faker = FakerUtil.buildFaker();
    return new SubjectRequest(
        subject.getName(),
        subject.getDescription(),
        faker.internet().userAgentAny(),
        subject.getCourse().getId(),
        subject.getSchedules().stream().map(ScheduleRequestMother::buildFrom).collect(Collectors.toList()));
  }
}
