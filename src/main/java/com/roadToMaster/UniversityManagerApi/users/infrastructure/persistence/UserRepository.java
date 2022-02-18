package com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence;

import com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntity, String> {

  Optional<UserEntity> findByUsername(String username);
}
