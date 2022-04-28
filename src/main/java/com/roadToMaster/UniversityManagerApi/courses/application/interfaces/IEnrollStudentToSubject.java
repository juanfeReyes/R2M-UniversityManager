package com.roadToMaster.UniversityManagerApi.courses.application.interfaces;

public interface IEnrollStudentToSubject {

  boolean execute(String subjectId, String studentId);
}
