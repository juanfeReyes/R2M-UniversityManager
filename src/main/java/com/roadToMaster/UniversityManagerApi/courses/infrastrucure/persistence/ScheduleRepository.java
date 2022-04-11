package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.ScheduleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ScheduleRepository extends JpaRepository<ScheduleEntity, String> {

  @Query("select s from ScheduleEntity as s where s.subject.id in :subjectIds")
  List<ScheduleEntity> findBySubjectId(@Param("subjectIds") List<String> subjectIds);
}
