package com.mavericks.springjwt.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.mavericks.springjwt.models.Order;
import com.mavericks.springjwt.models.User;
import com.mavericks.springjwt.payload.response.MessageResponse;
import com.mavericks.springjwt.repository.OrderRepository;
import com.mavericks.springjwt.repository.UserRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/order")
public class OrderController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{uid}")
    public ResponseEntity<?> postOrder(@RequestBody Order order, @PathVariable("uid") Long uid) {
        User user = userRepository.getUserById(uid);
        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found with id: " + uid));
        }
        order.setUser(user);
        orderRepository.save(order);
        return ResponseEntity.ok(new MessageResponse("Order created with success!"));
    }

    @GetMapping("/list")
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/{oid}")
    public ResponseEntity<Order> getOrderById(@PathVariable("oid") Long oid) {
        Order order = orderRepository.getOrderById(oid);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/status/{oid}")
    public ResponseEntity<Order> setOrderStatus(@PathVariable("oid") Long oid) {
        Order order = orderRepository.getOrderById(oid);
        order.setStatus("Canceled");
        return ResponseEntity.ok().body(orderRepository.save(order));
    }

    @DeleteMapping("/delete/{oid}")
    public ResponseEntity<MessageResponse> deleteById(@PathVariable("oid") Long oid) {
        if (orderRepository.getOrderById(oid) == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Order not found with id: " + oid));
        }
        orderRepository.deleteById(oid);
        return ResponseEntity.ok().body(new MessageResponse("Order deleted with success!"));
    }

}
