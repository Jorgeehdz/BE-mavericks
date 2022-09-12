package com.mavericks.springjwt.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.mavericks.springjwt.models.Order;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select o from Order o where o.id = ?1")
    Optional<Order> getOrderByIdOptional(Long oid);

    @Query("select o from Order o where o.id = ?1")
    Order getOrderById(Long oid);
}
