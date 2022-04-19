package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IGetSubjects {

  Page<Subject> execute(Pageable pageable);
}
