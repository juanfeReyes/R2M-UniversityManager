package com.roadToMaster.UniversityManagerApi.shared.security;

public enum SecurityAuthority {
  course_read("course.read"),
  course_write("course.write"),
  subject_write("subject.write");

  private final String item;

  SecurityAuthority(String item) {
    this.item = item;
  }

  public String getItem() {
    return this.item;
  }
}
