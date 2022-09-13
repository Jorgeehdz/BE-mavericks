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

import com.mavericks.springjwt.email.EmailDetails;
import com.mavericks.springjwt.email.EmailService;
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
    private EmailService emailService;

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{uid}/{email}")
    public ResponseEntity<?> postOrder(@RequestBody Order order,
            @PathVariable("email") String email,
            @PathVariable("uid") Long uid) {
        User user = userRepository.getUserById(uid);
        EmailDetails mail = new EmailDetails();

        if (user == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: User not found with id: " + uid));
        }
        order.setUser(user);
        orderRepository.save(order);
        mail.setRecipient(email);
        mail.setMsgBody("Your Order " + order.getId() + " Was created Successfully");
        mail.setSubject("Order Created");
        emailService.sendSimpleMail(mail);
        return ResponseEntity.ok(new MessageResponse("Order created with success!"));
    }

    @GetMapping("/list")
    public List<Order> getOrders() {
        return orderRepository.findAll();
    }

    @GetMapping("/list/{uid}")
    public List<Order> getOrdersByUserId(@PathVariable("uid") Long uid) {
        return orderRepository.getOrdersByUserId(uid);
    }

    @GetMapping("/{oid}")
    public ResponseEntity<Order> getOrderById(@PathVariable("oid") Long oid) {
        Order order = orderRepository.getOrderById(oid);
        return ResponseEntity.ok(order);
    }

    @PutMapping("/status/{oid}/{email}")
    public ResponseEntity<?> setOrderStatus(@PathVariable("oid") Long oid, @PathVariable("email") String email) {
        Order order = orderRepository.getOrderById(oid);
        EmailDetails mail = new EmailDetails();
        String status = order.getStatus();
        if (status.equals("Created")) {
            order.setStatus("Cancelled");
            orderRepository.save(order);
            mail.setRecipient(email);
            mail.setMsgBody("Your Order " + order.getId() + " Was Cancelled");
            mail.setSubject("Order Cancelled");
            emailService.sendSimpleMail(mail);
            return ResponseEntity.ok().body("Order Status Changed With Success!");
        } else {
            order.setStatus("Created");
            orderRepository.save(order);
            mail.setRecipient(email);
            mail.setMsgBody("Your Order " + order.getId() + " Was Created");
            mail.setSubject("Order Created");
            emailService.sendSimpleMail(mail);
            return ResponseEntity.ok().body("Order Status Changed With Success!");
        }

    }

    @DeleteMapping("/delete/{oid}/{email}")
    public ResponseEntity<MessageResponse> deleteById(@PathVariable("oid") Long oid,
            @PathVariable("email") String email) {
        Order order = orderRepository.getOrderById(oid);
        if (order == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Order not found with id: " + oid));
        }
        EmailDetails mail = new EmailDetails();
        orderRepository.deleteById(oid);
        mail.setRecipient(email);
        mail.setMsgBody("Your Order " + order.getId() + " Was Deleted");
        mail.setSubject("Order Deleted");
        emailService.sendSimpleMail(mail);

        return ResponseEntity.ok().body(new MessageResponse("Order deleted with success!"));
    }

}
