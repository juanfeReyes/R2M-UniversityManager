package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

  @Query("select s from SubjectEntity as s where s.professor.username = :username")
  List<SubjectEntity> findByProfessorUsername(@Param("username") String username);

  @Query("select s from SubjectEntity as s where course.id = :courseId")
  List<SubjectEntity> findAllByCourse(@Param("courseId") String courseId);

  @Query("select s from SubjectEntity as s inner join s.students students where students.id in :studentId")
  List<SubjectEntity> findAllByStudent(@Param("studentId") String courseId);

  @Query("select s from SubjectEntity as s where s.name = :name and course.id = :courseId")
  Optional<SubjectEntity> findByNameAndCourse(@Param("name") String name, @Param("courseId") String courseId);
}
