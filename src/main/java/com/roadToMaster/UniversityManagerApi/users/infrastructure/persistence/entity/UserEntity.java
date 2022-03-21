package com.roadToMaster.UniversityManagerApi.users.infrastructure.persistence.entity;

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
}
