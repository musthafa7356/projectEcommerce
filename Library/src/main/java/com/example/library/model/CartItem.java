package com.example.library.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Entity
@Table(name = "cart")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne (fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn (name = "shopping_cart_id", referencedColumnName = "shopping_cart_id")
    @ToString.Exclude
    private ShoppingCart cart;

    @OneToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "product_id", referencedColumnName = "product_id")
    @ToString.Exclude
    private Product product;

    @Min(value = 1)
    private int quantity;

    private double unitPrice;

    @ManyToOne (fetch = FetchType.EAGER)
    @JoinColumn (name = "customer_id", referencedColumnName = "customer_id")
    private Customer customer;

    @Override
    public String toString(){
        return "cart{" +
                "id=" + id +
                ", shoppingCart=" + cart.getId() +
                ", product=" + product.getName() +
                ", quantity=" + quantity +
                ", unitPrice=" + unitPrice +
                '}';
    }
}
