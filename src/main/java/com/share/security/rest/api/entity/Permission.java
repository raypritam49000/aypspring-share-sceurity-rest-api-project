package com.share.security.rest.api.entity;

import com.share.security.rest.api.entity.base.AuditableBaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Where;

@EqualsAndHashCode(callSuper = true)
@Entity
@Where(clause="deleted=0")
@Table(name = "permission_tb")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Permission extends AuditableBaseEntity {
    private String abbr;
    private String name;
}