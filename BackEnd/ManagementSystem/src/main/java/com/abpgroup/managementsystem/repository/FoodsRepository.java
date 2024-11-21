package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Foods;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodsRepository extends JpaRepository <Foods, Long> {
}
