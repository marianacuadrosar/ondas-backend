package com.hidroterapia_ondas.repository;
import com.hidroterapia_ondas.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {

}
