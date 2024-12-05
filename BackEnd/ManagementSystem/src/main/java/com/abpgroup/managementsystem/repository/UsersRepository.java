package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersRepository extends JpaRepository<Users, Long> {

    @Query("SELECT u FROM Users u WHERE u.email = :email")
    Users findByEmail(String email);

    @Query("SELECT u FROM Users u WHERE lower(u.name) LIKE lower(CONCAT('%', :name, '%'))")
    Page<Users> getUsersByName(String name, Pageable pageable);
}
