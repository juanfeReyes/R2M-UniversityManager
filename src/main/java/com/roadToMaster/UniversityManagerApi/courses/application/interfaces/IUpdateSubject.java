package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

import com.roadToMaster.UniversityManagerApi.courses.domain.Schedule;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;

import java.util.List;

public interface IUpdateSubject {

  Subject execute(String id, String name, String description, String courseId,
                  String professorUsername, List<Schedule> schedules);
}
