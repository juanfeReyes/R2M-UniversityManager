package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

public interface IRemoveStudentFromSubject {

  boolean execute(String subjectId, String studentId);
}
