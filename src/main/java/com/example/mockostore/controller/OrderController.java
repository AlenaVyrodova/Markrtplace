package com.example.mockostore.controller;

import com.example.mockostore.dto.order.OrderCreateRequestShippingAddressDto;
import com.example.mockostore.dto.order.OrderItemResponseDto;
import com.example.mockostore.dto.order.OrderResponseDto;
import com.example.mockostore.dto.order.OrderUpdateRequestStatusDto;
import com.example.mockostore.model.User;
import com.example.mockostore.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(value = "/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{itemId}")
    @Operation(summary = "Get order item from order",
            description = "Get specific order item from order")
    public OrderItemResponseDto getOrderItemFromOrder(Authentication authentication,
                                                      @PathVariable Long orderId,
                                                      @PathVariable Long itemId) {
        User user = (User) authentication.getPrincipal();
        return orderService.findOrderItemInOrder(user.getId(), orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    @Operation(summary = "Update order status", description = "Update order status")
    public OrderResponseDto updateOrderStatus(@PathVariable Long id,
                                              @RequestBody
                                              @Valid OrderUpdateRequestStatusDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping
    @Operation(summary = "Get order history", description = "Get user's order history")
    public List<OrderResponseDto> getAllUserOrders(Authentication authentication,
                                                   Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllUsersOrders(user, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @GetMapping("/{id}/items")
    @Operation(summary = "Get order items from order",
            description = "Get user's order items from order")
    public List<OrderItemResponseDto> getOrderItemsFromOrder(Authentication authentication,
                                                             @PathVariable Long id,
                                                             Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getAllOrderItemsFromOrder(user.getId(), id, pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new order", description = "Create new order")
    public OrderResponseDto createNewOrder(Authentication authentication,
                                           @RequestBody @Valid OrderCreateRequestShippingAddressDto
                                                   requestDto) {
        User user = (User) authentication.getPrincipal();
        return orderService.createOrder(user, requestDto);
    }
}