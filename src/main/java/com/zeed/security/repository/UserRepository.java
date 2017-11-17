package com.zeed.security.repository;

import com.zeed.models.*;
import org.springframework.data.jpa.repository.JpaRepository;


/**
 * Created by stephan on 20.03.16.
 */
public interface UserRepository extends JpaRepository<com.zeed.models.User, Long> {
    com.zeed.models.User findByUsername(String username);
    com.zeed.models.User findById(Long Id);
}
