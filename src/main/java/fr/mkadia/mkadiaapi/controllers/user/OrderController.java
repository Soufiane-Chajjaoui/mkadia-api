package fr.mkadia.mkadiaapi.controllers.user;


import fr.mkadia.mkadiaapi.dtos.OrderDTO;
import fr.mkadia.mkadiaapi.entities.Order;
import fr.mkadia.mkadiaapi.entities.User;
import fr.mkadia.mkadiaapi.enums.OrderStatus;
import fr.mkadia.mkadiaapi.models.OrderItemRequest;
import fr.mkadia.mkadiaapi.services.order.IOrderService;
import fr.mkadia.mkadiaapi.services.order.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final IOrderService orderService;

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@AuthenticationPrincipal User user,
                                                @RequestBody List<OrderItemRequest> items) {
        OrderDTO order = orderService.createOrder(user, items);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrder(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrder(id));
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(@PathVariable Integer id,
                                              @RequestParam OrderStatus status) {
        return ResponseEntity.ok(orderService.updateOrderStatus(id, status));
    }
}
