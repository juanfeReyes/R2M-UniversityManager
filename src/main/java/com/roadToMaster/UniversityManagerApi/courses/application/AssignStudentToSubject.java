package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IAssignStudentToSubject;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AssignStudentToSubject implements IAssignStudentToSubject {

  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

  @Autowired
  public AssignStudentToSubject(SubjectRepository subjectRepository, UserRepository userRepository) {
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
  }

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

    //verify overlap of subjects of a student

    var subject = subjectEntity.get();
    subject.getStudents().add(studentEntity.get());
    subjectRepository.save(subject);

    return false;
  }
}
