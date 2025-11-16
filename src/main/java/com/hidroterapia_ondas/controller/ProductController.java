package com.hidroterapia_ondas.controller;

import com.hidroterapia_ondas.model.Product;
import com.hidroterapia_ondas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    // Publico: listado de productos
    @GetMapping
    public List<Product> list() {
        return productRepo.findAll();
    }

    // Solo admins pueden crear nuevos productos
    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public Product create(@RequestBody Product p) {
        return productRepo.save(p);
    }
}
