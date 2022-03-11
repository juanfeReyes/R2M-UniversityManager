package com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity;

import com.roadToMaster.UniversityManagerApi.users.domain.RoleEnum;
import com.roadToMaster.UniversityManagerApi.users.domain.User;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "userTable")
public class UserEntity {

  @Id
  private String id;

  @Column
  private String username;

  @Column
  private String firstName;

  @Column
  private String lastName;

  @Column
  private String email;

  @Column
  private String role;

  @Column
  private boolean active;

  public static UserEntity toEntity(User user) {
    return UserEntity.builder()
        .id(user.getId())
        .username(user.getUsername())
        .firstName(user.getFirstName())
        .lastName(user.getLastName())
        .email(user.getEmail())
        .role(user.getRole().toString())
        .active(user.isActive())
        .build();
  }

  public static User toDomain(UserEntity userEntity) {
    return new User(
        userEntity.getId(),
        userEntity.getUsername(),
        userEntity.getFirstName(),
        userEntity.getLastName(),
        userEntity.getEmail(),
        RoleEnum.valueOf(userEntity.getRole()),
        userEntity.isActive());
  }
}
