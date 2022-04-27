package com.roadToMaster.UniversityManagerApi.courses.application;

import com.roadToMaster.UniversityManagerApi.courses.application.interfaces.IGetStudent;
import com.roadToMaster.UniversityManagerApi.courses.domain.Student;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.SubjectRepository;
import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CoursesEntityMapper;
import com.roadToMaster.UniversityManagerApi.shared.domain.exceptions.ResourceNotFoundException;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.UserRepository;
import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntityMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
public class GetStudent implements IGetStudent {

  private final SubjectRepository subjectRepository;

  private final UserRepository userRepository;

  private final CoursesEntityMapper coursesMapper;

  private final UserEntityMapper userEntityMapper;

  @Autowired
  public GetStudent(SubjectRepository subjectRepository, UserRepository userRepository, CoursesEntityMapper coursesMapper, UserEntityMapper userEntityMapper) {
    this.subjectRepository = subjectRepository;
    this.userRepository = userRepository;
    this.coursesMapper = coursesMapper;
    this.userEntityMapper = userEntityMapper;
  }

  public Student execute(String username) {

    //Verify student must have role STUDENT
    var userEntity = userRepository.findByUsername(username);
    if(userEntity.isEmpty()){
      throw new ResourceNotFoundException("Student does not exists");
    }
    var studentProfile = userEntityMapper.userToDomain(userEntity.get());
    var subjectEntities = subjectRepository.findAllByStudent(studentProfile.getId());

    var subjects = subjectEntities.stream().map(entity -> {
      var schedules = entity.getSchedules().stream().map(coursesMapper::scheduleToDomain).collect(Collectors.toList());
      return coursesMapper.subjectToDomain(entity, schedules);
    }).collect(Collectors.toList());

    return new Student(studentProfile, subjects);
  }
}
