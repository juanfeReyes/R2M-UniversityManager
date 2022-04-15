package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class DeleteSubject implements IDeleteSubject {

  private final SubjectRepository subjectRepository;

  @Autowired
  public DeleteSubject(SubjectRepository subjectRepository) {
    this.subjectRepository = subjectRepository;
  }

  @Transactional
  public void execute(String subjectId) {

    if (!subjectRepository.existsById(subjectId)) {
      throw new ResourceNotFoundException(String.format("Subject with id: %s does not exists", subjectId));
    }

    subjectRepository.deleteById(subjectId);
  }
}
