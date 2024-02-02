package com.example.library.repository;

import com.example.library.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, Long> {

    Offer findById(long id);

    @Query(value = "SELECT * FROM offers p WHERE p.name = :name AND p.enabled = true", nativeQuery = true)
    List<Offer> findByNameAndEnabledIs(@Param("name") String name);
}
