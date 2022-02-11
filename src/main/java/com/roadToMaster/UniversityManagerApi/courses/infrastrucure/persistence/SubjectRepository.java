package com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence;

import com.roadToMaster.UniversityManagerApi.courses.infrastrucure.persistence.entity.SubjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubjectRepository extends JpaRepository<SubjectEntity, String> {

}
