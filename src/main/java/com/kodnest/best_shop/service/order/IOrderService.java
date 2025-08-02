package com.kodnest.best_shop.service.order;

import com.kodnest.best_shop.dto.OrderDto;
import com.kodnest.best_shop.model.Order;

import java.util.List;

public interface IOrderService {
    Order placeOrder(Long userId);
    OrderDto getOrder(Long orderId);

    List<OrderDto> getUserOrders(Long orderId);
}
