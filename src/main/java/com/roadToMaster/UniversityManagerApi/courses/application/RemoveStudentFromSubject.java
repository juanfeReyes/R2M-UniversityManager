package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IRemoveStudentFromSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RemoveStudentFromSubject implements IRemoveStudentFromSubject {
  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

  @Autowired
  public RemoveStudentFromSubject(SubjectRepository subjectRepository, UserRepository userRepository) {
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
  }

  @Override
  public boolean execute(String subjectId, String studentId) {
    var subjectEntity =  subjectRepository.findById(subjectId);
    if(subjectEntity.isEmpty()){
      throw new ResourceNotFoundException("Subject does not exists");
    }

    // Verify that is student by role!!
    var studentEntity = userRepository.findById(studentId);
    if(studentEntity.isEmpty()){
      throw new ResourceNotFoundException("Student does not exists");
    }

    var subject = subjectEntity.get();
    subject.getStudents().removeIf(userEntity -> userEntity.getId().equals(studentId));
    subjectRepository.save(subject);

    return false;
  }
}
