package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;

import java.util.List;

public interface ICreateSubject {

  Subject execute(String name, String description, String courseName,
                  String professorId, List<Schedule> schedules);
}
