package com.example.library.repository;

import com.example.library.model.Category;
import com.example.library.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    Optional<Product> findById(Long id);

    @Query("select p from Product p where p.is_deleted=false and p.is_activated= true")
    List<Product> getAllProduct();
    @Query("select p from Product p where p.name like%?1% or p.description like %?1%")
    List<Product> findAllByNameOrDescription(String keyword);

    @Query(value = "select * from products where is_activated = true and category_id = :id", nativeQuery = true)
    List<Product> findAllByActivatedTrue(@Param("id") long id);



    @Query(value = "select p.cost_price,p.name from products p JOIN categories c ON p.category_id= c.category_id WHERE p.is_activated = true and c.is_activated=true", nativeQuery = true)
    List<Product> findAllByCatActivatedTrue();




    @Query(value = "select * from products where is_activated = true order by case when :sort = 'lowToHigh' then cost_price end asc , case when :sort = 'highToLow' then cost_price end desc ", nativeQuery = true)
    List<Product> findAllByActivatedTrueAndSortBy(@Param("sort") String sort);

    @Query(value = "select * from products where is_activated = true", nativeQuery = true)
    List<Product> findAllByActivatedTrue();

    @Query(value = "SELECT * FROM products p WHERE p.name = :name AND p.is_activated = true", nativeQuery = true)
    List<Product> findByNameAndIsActivated(@Param("name") String name);

    @Query(value = "select * from products ORDER BY product_id DESC",nativeQuery = true)
    List<Product> findAllByOrderById();

    @Query(value = "select count(*) from Product")
    Long CountAllProducts();

    @Query(value = "SELECT p.product_id, p.name, c.name, " +
            "SUM(od.quantity) AS total_quantity_ordered, SUM(od.quantity * p.cost_Price) AS total_revenue " +
            "FROM products p " +
            "JOIN order_Detail od ON p.product_id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'Delivered' " +
            "GROUP BY p.product_id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductStatsForConfirmedOrders();

    @Query(value = "SELECT p.product_id, p.name, c.name, " +
            "SUM(od.quantity) AS total_quantity_ordered, SUM(od.quantity * p.cost_Price) AS total_revenue " +
            "FROM products p " +
            "JOIN order_Detail od ON p.product_id = od.product_id " +
            "JOIN orders o ON od.order_id = o.order_id " +
            "JOIN categories c ON p.category_id = c.category_id " +
            "WHERE o.order_Status = 'Delivered' " +
            "AND o.order_date BETWEEN :startDate AND :endDate " +
            "GROUP BY p.product_id, p.name, c.name " +
            "ORDER BY total_revenue DESC",nativeQuery = true)
    List<Object[]> getProductsStatsForConfirmedOrdersBetweenDates(@Param("startDate") Date startDate, @Param("endDate") Date endDate);

    List<Product> findAllByCategoryId(long id);

    List<Product> findByNameStartingWithIgnoreCase(String keyword);
//    List<Product> findAllByNameContainingIgnoreCase(String keyword);
//    Product existsProductById(long id);

//    boolean existsById(long id);
    List<Product> findByCategory(Category category);
}
