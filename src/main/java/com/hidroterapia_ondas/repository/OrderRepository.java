package com.hidroterapia_ondas.repository;
import com.hidroterapia_ondas.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    // Filtrar por estado o por nombre de cliente
    List<Order> findByStatus(String status);
    List<Order> findByCustomerNameContainingIgnoreCase(String name);
}
