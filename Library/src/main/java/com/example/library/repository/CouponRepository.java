package com.example.library.repository;

import com.example.library.model.Coupon;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CouponRepository extends JpaRepository <Coupon, Long> {

    Coupon findCouponByCode(String code);

    Coupon findById(long id);
}
