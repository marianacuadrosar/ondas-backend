package com.hidroterapia_ondas.repository;

import com.hidroterapia_ondas.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByStatusIgnoreCase(String status);

    List<Order> findByCustomerNameContainingIgnoreCase(String customerName);
}


