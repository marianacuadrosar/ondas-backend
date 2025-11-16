package com.hidroterapia_ondas.controller;

import com.hidroterapia_ondas.model.Order;
import com.hidroterapia_ondas.model.OrderItem;
import com.hidroterapia_ondas.model.Product;
import com.hidroterapia_ondas.repository.OrderRepository;
import com.hidroterapia_ondas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderRepository orderRepo;
    @Autowired
    private ProductRepository productRepo;
    // Registrar un nuevo pedido (público, pero requiere autenticación de usuario)

    @PostMapping
    @PreAuthorize("hasAuthority('ROLE_USER') or hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
        Order order = new Order();
        order.setCustomerName(request.getName());
        order.setCustomerEmail(request.getEmail());
        order.setCustomerPhone(request.getPhone());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        // Construir items
        request.getItems().forEach(itemDto -> {
            Product p = productRepo.findById(itemDto.getProductId())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
                            OrderItem item = new OrderItem();
            item.setProduct(p);
            item.setQuantity(itemDto.getQuantity());
            item.setOrder(order);
            order.getItems().add(item);
        });
        Order saved = orderRepo.save(order);
        return ResponseEntity.ok(saved);
    }

    // Consultar todos los pedidos (solo admin)
    @GetMapping
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Order> listAll() {
        return orderRepo.findAll();
    }

    // Consultar por estado
    @GetMapping("/status/{status}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Order> listByStatus(@PathVariable String status) {
        return orderRepo.findByStatus(status.toUpperCase());
    }

    // Cambiar estado (pendiente -> atendido)
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<?> complete(@PathVariable Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Pedido no encontrado"));
                        order.setStatus("COMPLETED");
        orderRepo.save(order);
        return ResponseEntity.ok(order);
    }

    // DTO para recibir items del pedido
    public static class OrderRequest {
        private String name;
        private String email;
        private String phone;
        private List<ItemDto> items;

        // getters y setters
        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public List<ItemDto> getItems() {
            return items;
        }

        public void setItems(List<ItemDto> items) {
            this.items = items;
        }
    }

    public static class ItemDto {
        private Long productId;
        private int quantity;

        // getters y setters
        public Long getProductId() {
            return productId;
        }

        public void setProductId(Long productId) {
            this.productId = productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
    }
}

