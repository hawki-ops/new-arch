package com.siemens.trail.repository;

import com.siemens.trail.model.WebUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebUserRepository extends JpaRepository<WebUser, Long> {
    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}
