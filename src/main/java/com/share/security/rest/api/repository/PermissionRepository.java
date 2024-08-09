package com.share.security.rest.api.repository;

import com.share.security.rest.api.entity.Permission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PermissionRepository extends JpaRepository<Permission, String> {
    Optional<Permission> findByName(String name);

    List<Permission> findAllByOrderByNameAsc();

    Optional<Permission> findByNameAndAbbr(String name, String abbr);
}