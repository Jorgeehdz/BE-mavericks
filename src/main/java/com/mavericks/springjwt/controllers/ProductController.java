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
import com.mavericks.springjwt.models.Product;
import com.mavericks.springjwt.payload.response.MessageResponse;
import com.mavericks.springjwt.repository.OrderRepository;
import com.mavericks.springjwt.repository.ProductRepository;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/product")
public class ProductController {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private OrderRepository orderRepository;

    @PostMapping
    public ResponseEntity<?> postProduct(@RequestBody Product product) {
        productRepository.save(product);
        return ResponseEntity.ok(new MessageResponse("Product created with success!"));
    }

    @GetMapping("/list")
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    @GetMapping("/list/available")
    public List<Product> getProductsAvailable() {
        return productRepository.getProductsAvailable();
    }

    @GetMapping("/id/{pid}")
    public ResponseEntity<?> getProductById(@PathVariable("pid") Long pid) {
        Product product = productRepository.getProductById(pid);
        if (product == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product not found with Id: " + pid));
        }
        return ResponseEntity.ok().body(product);
    }

    @GetMapping("/name/{pname}")
    public ResponseEntity<?> getProductByName(@PathVariable("pname") String pname) {
        Product product = productRepository.getProductByName(pname);
        if (product == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product not found with name: " + pname));
        }
        return ResponseEntity.ok().body(product);
    }

    @PutMapping("/order/{pid}/{oid}")
    public ResponseEntity<?> setProductInOrder(@PathVariable("pid") Long pid, @PathVariable("oid") Long oid) {
        Order order = orderRepository.getOrderById(oid);
        if (order == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Order not found with Id: " + oid));
        }

        Product product = productRepository.getProductById(pid);
        if (product == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product not found with Id: " + pid));
        }

        product.setOrder(order);
        productRepository.save(product);
        return ResponseEntity.ok().body(new MessageResponse("Product added in order with id: " + oid));
    }

    @DeleteMapping("/delete/{pid}")
    public ResponseEntity<?> deteProductById(@PathVariable("pid") Long pid) {
        Product product = productRepository.getProductById(pid);
        if (product == null) {
            return ResponseEntity.badRequest().body(new MessageResponse("Product not found with Id: " + pid));
        }
        productRepository.deleteById(pid);
        return ResponseEntity.ok().body(new MessageResponse("Product deleted with success!"));
    }
}
