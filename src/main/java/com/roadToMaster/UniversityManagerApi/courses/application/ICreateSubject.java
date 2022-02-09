package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;

public interface ICreateSubject {

  Subject execute(String id, String name, String description, String courseName);
}
