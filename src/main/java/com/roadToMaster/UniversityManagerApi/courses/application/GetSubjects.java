package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IGetSubjects;
import com.roadToMaster.UniversityManagerApi.courses.domain.Subject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GetSubjects implements IGetSubjects {

  private final SubjectRepository subjectRepository;

  private final CoursesEntityMapper coursesMapper;

  @Autowired
  public GetSubjects(SubjectRepository subjectRepository, CoursesEntityMapper coursesMapper) {
    this.subjectRepository = subjectRepository;
    this.coursesMapper = coursesMapper;
  }

  @Override
  public Page<Subject> execute(Pageable pageable) {

    var entitiesPage = subjectRepository.findAll(pageable);

    return entitiesPage.map(entity -> {
      var schedules = entity.getSchedules().stream().map(coursesMapper::scheduleToDomain).collect(Collectors.toList());
      return coursesMapper.subjectToDomain(entity, schedules);
    });
  }
}
