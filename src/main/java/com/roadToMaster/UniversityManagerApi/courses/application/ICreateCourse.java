package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Course;

import javax.validation.Valid;

public interface ICreateCourse {
  Course execute(@Valid Course course) throws Exception;
}
