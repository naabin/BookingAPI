package com.reservation.models;

import java.time.OffsetDateTime;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonFormat;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class Auditable {
	
	
	@CreatedBy
	private String createdBy;
	
	@CreatedDate
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXX")
	private OffsetDateTime createdDate;
	
	@LastModifiedDate
	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXX")
	private OffsetDateTime lastModifiedDate;
	
	
	@LastModifiedBy
	private String lastModifiedBy;


	public String getCreatedBy() {
		return createdBy;
	}


	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}


	public OffsetDateTime getCreatedDate() {
		return createdDate;
	}


	public void setCreatedDate(OffsetDateTime createdDate) {
		this.createdDate = createdDate;
	}


	public OffsetDateTime getLastModifiedDate() {
		return lastModifiedDate;
	}


	public void setLastModifiedDate(OffsetDateTime lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}


	public String getLastModifiedBy() {
		return lastModifiedBy;
	}


	public void setLastModifiedBy(String lastModifiedBy) {
		this.lastModifiedBy = lastModifiedBy;
	}
	
	
	

}
