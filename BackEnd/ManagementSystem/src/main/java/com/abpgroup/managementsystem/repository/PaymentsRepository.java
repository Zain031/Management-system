package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {

    @Query("SELECT p FROM Payments p WHERE p.order.idOrder= :orderId")
    Payments findByOrderId(Long orderId);
}
