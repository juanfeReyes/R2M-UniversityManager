package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;

import java.util.List;

public interface ICreateSubject {

  Subject execute(String id, String name, String description, String courseName,
                  String professorId, List<Schedule> schedules);
}
