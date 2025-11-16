package com.hidroterapia_ondas.repository;
import com.hidroterapia_ondas.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

}