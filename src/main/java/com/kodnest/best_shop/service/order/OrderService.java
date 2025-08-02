package com.kodnest.best_shop.service.order;

import com.kodnest.best_shop.dto.OrderDto;
import com.kodnest.best_shop.dto.OrderItemDto;
import com.kodnest.best_shop.enums.OrderStatus;
import com.kodnest.best_shop.exceptions.ResourceNotFoundException;
import com.kodnest.best_shop.model.Cart;
import com.kodnest.best_shop.model.Order;
import com.kodnest.best_shop.model.OrderItem;
import com.kodnest.best_shop.model.Product;
import com.kodnest.best_shop.repository.OrderRepository;
import com.kodnest.best_shop.repository.ProductRepository;
import com.kodnest.best_shop.service.cart.CartService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService implements IOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;


    @Override
    @Transactional
    public Order placeOrder(Long userId) {
        Cart cart = cartService.getCart(userId);
        Order order = createOrder(cart);
        List<OrderItem> orderItems = createOrderItems(order, cart);
        order.setOrderItems(new HashSet<>(orderItems));
        order.setOrderTotalAmount(calculateTotalAmount(orderItems));
        Order savedOrder = orderRepository.save(order);
        cartService.clearCart(cart.getId());
        return null;
    }

    @Override
    public OrderDto getOrder(Long orderId) {
        return orderRepository.findById(orderId).map(
                this::convertToDto
        ).orElseThrow(()-> new ResourceNotFoundException("Order not found"));
    }

    private Order createOrder(Cart cart) {
        Order order = new Order();
        order.setUser(cart.getUser());
        order.setOrderDate(LocalDate.now());
        order.setOrderStatus(OrderStatus.PENDING);
        return order;
    }

    private BigDecimal calculateTotalAmount(List<OrderItem> orderItemList) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        return orderItemList
                .stream()
                .map(item -> item.getPrice()
                        .multiply(new BigDecimal(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private List<OrderItem> createOrderItems(Order order, Cart cart) {
        return cart.getItems().stream().map(cartItem -> {
            Product product = cartItem.getProduct();
            product.setInventory(product.getInventory() - cartItem.getQuantity());
            productRepository.save(product);
            return new OrderItem(
                    order,
                    product,
                    cartItem.getQuantity(),
                    cartItem.getUnitPrice());
        }).toList();
    }

    @Override
    public List<OrderDto> getUserOrders(Long userId) {
        List<Order> orders = orderRepository.findByUserId(userId);
        return orders.stream().map(this::convertToDto).collect(Collectors.toList());
    }



    private OrderDto convertToDto(Order order) {
        OrderDto dto = new OrderDto();
        dto.setId(order.getId());
        dto.setUserId(order.getUser().getId());  // extract user ID
        dto.setOrderDate(order.getOrderDate().atStartOfDay());  // Convert LocalDate to LocalDateTime
        dto.setTotalAmount(order.getOrderTotalAmount());
        dto.setStatus(order.getOrderStatus().name()); // assuming status in DTO is String

        // Convert Set<OrderItem> to List<OrderItemDto>
        List<OrderItemDto> itemDtos = new ArrayList<>();
        for (OrderItem item : order.getOrderItems()) {
            OrderItemDto itemDto = new OrderItemDto();
            itemDto.setProductId(item.getProduct().getId());      // Assuming OrderItem has Product
            itemDto.setProductName(item.getProduct().getName());  // Assuming OrderItem has Product
            itemDto.setQuantity(item.getQuantity());
            itemDto.setPrice(item.getPrice());
            itemDtos.add(itemDto);
        }
        dto.setOrderItems(itemDtos);
        return dto;
    }
}
