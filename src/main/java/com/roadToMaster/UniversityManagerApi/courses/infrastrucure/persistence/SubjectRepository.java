package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

  @Query("select s from SubjectEntity as s where s.professor.username = :username")
  List<SubjectEntity> findByProfessorUsername(@Param("username") String username);

}
