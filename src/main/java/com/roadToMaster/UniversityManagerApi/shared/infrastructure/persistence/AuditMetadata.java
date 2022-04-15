package com.roadToMaster.UniversityManagerApi.shared.infrastructure.persistence;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class AuditMetadata {

  @CreatedDate
  @Column(name = "created_date", nullable = false, updatable = false)
  private Date createdDate;

  @LastModifiedDate
  private Date updatedDate;
}
