package orderingservice.controllers;

import orderingservice.dtos.OrderCreateDTO;
import orderingservice.dtos.OrderDetailsDTO;
import orderingservice.dtos.OrderResponseDTO;
import orderingservice.models.Order;
import orderingservice.services.OrderingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("order")
public class OrderingController {

    private final OrderingService orderingService;

    @Autowired
    public OrderingController(OrderingService orderingService) {
        this.orderingService = orderingService;
    }


    @GetMapping("/{orderId}")
    public Mono<OrderDetailsDTO> getOrderDetails(@PathVariable String orderId) {
        return orderingService.getOrderDetails(orderId);
    }

    @GetMapping("/user/{userId}")
    public Flux<OrderResponseDTO> getOrdersByUser(@PathVariable String userId) {
        return orderingService.getOrdersByUser(userId);
    }

    @PostMapping
    public Mono<Order> createOrder(@RequestBody Mono<OrderCreateDTO> orderCreateDTOMono) {
        return orderingService.createOrder(orderCreateDTOMono);
    }

    @PatchMapping("/{orderId}/cancel")
    public Mono<Void> cancelOrder(@PathVariable String orderId) {
        return orderingService.cancelOrder(orderId);
    }

    @PatchMapping("/{orderId}/finish")
    public Mono<Void> finishOrder(@PathVariable String orderId) {
        return orderingService.finishOrder(orderId);
    }


}
