package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IGetSubjects {

  List<Subject> execute(String courseName, Pageable pageable);
}
