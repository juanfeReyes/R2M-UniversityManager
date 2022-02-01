package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;

public interface ICreateCourse {
  Course execute(Course course) throws Exception;
}
