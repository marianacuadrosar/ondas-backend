package com.hidroterapia_ondas.controller;

import com.hidroterapia_ondas.model.Order;
import com.hidroterapia_ondas.model.OrderItem;
import com.hidroterapia_ondas.model.Product;
import com.hidroterapia_ondas.repository.OrderRepository;
import com.hidroterapia_ondas.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*")
public class OrderController {

    @Autowired
    private OrderRepository orderRepo;

    @Autowired
    private ProductRepository productRepo;

    // Registrar un nuevo pedido (público: el cliente no tiene que loguearse)
    @PostMapping
    public ResponseEntity<Order> createOrder(@RequestBody OrderRequest request) {

        if (request == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Cuerpo de la petición vacío");
        }

        Order order = new Order();
        order.setCustomerName(request.getName());
        order.setCustomerEmail(request.getEmail());
        order.setCustomerPhone(request.getPhone());
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());

        List<OrderItem> items = new ArrayList<>();

        if (request.getItems() != null) {
            for (ItemDto itemReq : request.getItems()) {

                Product product = productRepo.findById(itemReq.getProductId())
                        .orElseThrow(() -> new ResponseStatusException(
                                HttpStatus.BAD_REQUEST,
                                "Producto no encontrado con id: " + itemReq.getProductId()
                        ));

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(itemReq.getQuantity());

                items.add(item);
            }
        }

        order.setItems(items);

        Order saved = orderRepo.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
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
        return orderRepo.findByStatusIgnoreCase(status);
        // Asegúrate de que este método exista en OrderRepository
        // List<Order> findByStatusIgnoreCase(String status);
    }

    // Consultar por cliente
    @GetMapping("/customer/{name}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public List<Order> listByCustomer(@PathVariable String name) {
        return orderRepo.findByCustomerNameContainingIgnoreCase(name);
        // Asegúrate de que este método exista en OrderRepository
        // List<Order> findByCustomerNameContainingIgnoreCase(String name);
    }

    // Cambiar estado (pendiente -> atendido)
    @PutMapping("/{id}/complete")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Order> complete(@PathVariable Long id) {
        Order order = orderRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Pedido no encontrado"
                ));
        order.setStatus("COMPLETED");
        Order saved = orderRepo.save(order);
        return ResponseEntity.ok(saved);
    }

    // ===== DTOs internos =====

    public static class OrderRequest {
        private String name;
        private String email;
        private String phone;
        private List<ItemDto> items;

        public String getName() { return name; }
        public void setName(String name) { this.name = name; }

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }

        public String getPhone() { return phone; }
        public void setPhone(String phone) { this.phone = phone; }

        public List<ItemDto> getItems() { return items; }
        public void setItems(List<ItemDto> items) { this.items = items; }
    }

    public static class ItemDto {
        private Long productId;
        private int quantity;

        public Long getProductId() { return productId; }
        public void setProductId(Long productId) { this.productId = productId; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
    }
}
