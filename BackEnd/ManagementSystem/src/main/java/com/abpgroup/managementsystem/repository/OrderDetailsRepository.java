package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.OrderDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderDetailsRepository extends JpaRepository<OrderDetails, Long> {
    @Query("SELECT od FROM OrderDetails od WHERE od.order.idOrder = :id")
    List<OrderDetails> findByOrderIdOrder(Long id);
}
