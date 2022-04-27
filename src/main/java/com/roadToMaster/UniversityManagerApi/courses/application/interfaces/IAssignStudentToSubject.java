package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

public interface IAssignStudentToSubject {

  boolean execute(String subjectId, String studentId);
}
