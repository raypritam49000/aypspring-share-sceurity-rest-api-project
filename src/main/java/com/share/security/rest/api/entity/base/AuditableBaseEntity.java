package com.share.security.rest.api.entity.base;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
@NoArgsConstructor
@MappedSuperclass
public abstract class AuditableBaseEntity extends BaseEntity {
    @Column(updatable = false)
    @CreationTimestamp
    protected Date creationDate;
    @UpdateTimestamp
    protected Date modifiedDate;
    @Column(updatable = false)
    protected String createdBy;
    protected String modifiedBy;
    protected boolean deleted;
}