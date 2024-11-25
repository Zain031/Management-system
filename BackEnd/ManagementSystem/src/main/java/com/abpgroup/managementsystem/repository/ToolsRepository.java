package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Tools;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ToolsRepository extends JpaRepository <Tools, Long> {
}
