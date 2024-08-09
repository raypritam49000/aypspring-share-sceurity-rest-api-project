package com.share.security.rest.api.repository;

import com.share.security.rest.api.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserEntityRepository extends JpaRepository<User, String> {
    Optional<User> findByUsernameOrEmailAndDeletedFalse(String username, String email);

    Optional<User> findByIdAndDeletedFalse(String id);

    User findByUsernameAndDeletedFalse(String id);

    Page<User> findAllByDeletedFalse(Pageable pageable);

    Page<User> findAllByTenantAndDeletedFalse(String tenant, Pageable pageable);

    Optional<User> findByEmailAndDeletedFalse(String email);

}