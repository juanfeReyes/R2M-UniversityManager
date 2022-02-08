package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.CourseEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<CourseEntity, String> {

  Optional<CourseEntity> findByName(String name);
}
