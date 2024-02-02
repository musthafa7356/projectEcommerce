package com.example.library.repository;

import com.example.library.model.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface SizeRepository extends JpaRepository<Size, Long> {

    Size findById(long id);

    @Modifying
    @Query(value = "delete from product_sizes where product_id = :id and size_id= :size_id",nativeQuery = true)
    void deleteSizesByProductIdAndSizeId(@Param("id")long id, @Param("size_id")long size_id);
}
