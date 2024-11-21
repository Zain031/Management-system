package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UsersRepository extends JpaRepository<Users, Long> {
    Users findByEmail(String email);
}
