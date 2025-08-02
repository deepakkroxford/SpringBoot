package com.kodnest.best_shop.controller;

import com.kodnest.best_shop.dto.OrderDto;
import com.kodnest.best_shop.exceptions.ResourceNotFoundException;
import com.kodnest.best_shop.model.Order;
import com.kodnest.best_shop.repository.UserRepository;
import com.kodnest.best_shop.response.ApiResponse;
import com.kodnest.best_shop.service.order.IOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/orders")
public class OrderController {
   private final IOrderService orderService;

   @PostMapping("/order")
   public ResponseEntity<ApiResponse> createOrder(@RequestParam Long userId) {
       try {
           Order order = orderService.placeOrder(userId);
           return ResponseEntity.ok(new ApiResponse("Item Order Success",order));
       }catch (Exception e) {
           return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new ApiResponse("Error Occured", e.getMessage()));
       }

   }

   @GetMapping("/{orderId}/order")
   public ResponseEntity<ApiResponse> getOrderById(@PathVariable Long orderId) {
       try {
           OrderDto order = orderService.getOrder(orderId);
           return ResponseEntity.ok(new ApiResponse("Item Order Success",order));
       }catch (ResourceNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!",e.getMessage()));
       }
   }

   @GetMapping("{userId}/orders")
   public ResponseEntity<ApiResponse> getUserOrders(@PathVariable Long userId) {
       try{
           List<OrderDto> orders = orderService.getUserOrders(userId);
           return ResponseEntity.ok(new ApiResponse("Item Order Success",orders));
       }catch (ResourceNotFoundException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ApiResponse("Oops!",e.getMessage()));
       }


   }


}
