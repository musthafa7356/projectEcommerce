package com.example.library.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "coupons")
public class Coupon {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupen_id")
    private long id;
    private String code;
    private String description;
    private int count;
    private int offPercentage;

    private int maxoff;

//    @Column(name = "min_order_amount")
    private int minOrderAmount;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate expiryDate;

    private boolean enabled;

    public boolean isExpired(){
        return (this.count == 0 || this.expiryDate.isBefore(LocalDate.now()));
    }

    @Override
    public String toString(){
        return "Coupon{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", description='" + description + '\'' +
                ", count=" + count +
                ", offPercentage=" + offPercentage +
                ", maxOff=" + maxoff +
                ", expiryDate=" + expiryDate +
                ", enabled=" + enabled +
                ", minOrderAmount=" + minOrderAmount +
                '}';
    }
}
