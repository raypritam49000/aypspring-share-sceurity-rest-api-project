package com.share.security.rest.api.entity;

import com.share.security.rest.api.entity.base.AuditableBaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.Where;

import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role_tb")
@Where(clause = "deleted=0")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Role extends AuditableBaseEntity {
    private String name;

    @ManyToMany(cascade = {CascadeType.ALL})
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JoinTable(name = "role_permissions_tb",
            joinColumns = @JoinColumn(name = "role_id", referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id", referencedColumnName = "id"))
    private Set<Permission> permissions;

    @Column(name = "authority_level")
    private int authorityLevel;
}