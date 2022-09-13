package com.mavericks.springjwt.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.mavericks.springjwt.models.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("select p from Product p where p.name=?1")
    Product getProductByName(String pname);

    @Query("select p from Product p where p.name=?1")
    Optional<Product> getProductByNameToCompare(String pname);

    @Query("select p from Product p where p.id = ?1")
    Product getProductById(Long pid);

    @Query("select p from Product p where p.id = ?1")
    Product getProductByIdToOrder(Long pid);

    @Query(value = "SELECT * FROM products where order_id IS NULL", nativeQuery = true)
    List<Product> getProductsAvailable();

    @Query(value = "SELECT * FROM products where order_id = :id", nativeQuery = true)
    List<Product> getProductsByOrder(@Param("id") Long id);
}
