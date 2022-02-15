package com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence;

import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
}
