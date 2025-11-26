package com.hidroterapia_ondas.model;

import jakarta.persistence.*;

@Entity
@Table(name = "order_items")
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Pedido al que pertenece este ítem
    @ManyToOne
    @JoinColumn(name = "order_id")
    private Order order;

    // Producto asociado
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    // Cantidad de ese producto en el pedido
    private int quantity;

    // ========= GETTERS y SETTERS =========

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
