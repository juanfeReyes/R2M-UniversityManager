package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;

public interface IUpdateCourse {

  Course execute(Course course);
}
