package com.abpgroup.managementsystem.repository;

import com.abpgroup.managementsystem.model.entity.Payments;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentsRepository extends JpaRepository<Payments, Long> {


    @Query("SELECT o FROM Payments o WHERE o.order.idOrder=:id")
    Payments getPaymentsByOrderId(String id);


    @Query("SELECT o FROM Payments o WHERE o.idPayment=:idPayment")
    Payments getPaymentsByIdPayment(Long idPayment);


    @Query("SELECT p FROM Payments p ORDER BY p.paymentDate DESC")
    Page<Payments> findAllOrderByDatePaymentNow(Pageable pageable);

    @Query("SELECT p FROM Payments p WHERE p.order.idOrder IN :orderIds")
    List<Payments> findQrisResponseByOrderId(@Param("orderIds") List<String> orderIds);

    @Query("SELECT p FROM Payments p WHERE p.order.idOrder IN :orderIds")
    List<Payments> findMethodByOrderId(@Param("orderIds") List<String> orderIds);
}
