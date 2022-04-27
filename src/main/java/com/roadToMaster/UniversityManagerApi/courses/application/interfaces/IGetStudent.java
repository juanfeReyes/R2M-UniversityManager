package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

import com.roadToMaster.UniversityManagerApi.courses.domain.Student;

public interface IGetStudent {

  Student execute(String studentId);
}
