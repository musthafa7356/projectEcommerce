package com.example.library.repository;

import com.example.library.model.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Long> {

    List<Color> findAllById(long id);

    @Modifying
    @Query(value = "delete from product_colors where product_id = :id and color_id = :color_id", nativeQuery = true)
    void deleteColorsByProductIdAndColorId(@Param("id") long id, @Param("color_id") long color_id);
}
