package orderingservice.utils;

import orderingservice.dtos.OrderCreateDTO;
import orderingservice.dtos.OrderDetailsDTO;
import orderingservice.dtos.OrderResponseDTO;
import orderingservice.dtos.TrackingRequestDTO;
import orderingservice.models.Order;

import java.time.LocalDateTime;

public class Mapper {

    public static Order mapOrderCreationDtoToEntity(OrderCreateDTO orderCreateDTO) {

        Order order = new Order();
        order.setSourcePost(orderCreateDTO.getSourcePost());
        order.setDestinationPost(orderCreateDTO.getDestinationPost());

        order.setTimestamp(LocalDateTime.now());
        order.setOrderStatus(OrderStatus.IN_PROGRESS);

        order.setUserId(orderCreateDTO.getUserId());
        order.setUserName(orderCreateDTO.getUserName());
        order.setUserLastName(orderCreateDTO.getUserLastName());
        order.setUserAddress(orderCreateDTO.getUserAddress());

        order.setProductName(orderCreateDTO.getProductName());
        order.setProductWeight(orderCreateDTO.getProductWeight());
        order.setTypeOfService(TypeOfService.valueOf(orderCreateDTO.getTypeOfService()));

        return order;
    }

    public static TrackingRequestDTO orderEntityToTrackingRequestDTO(Order order, String status) {
        TrackingRequestDTO trackingRequestDTO = new TrackingRequestDTO();
        trackingRequestDTO.setOrderId(order.getId());
        trackingRequestDTO.setCurrentPost(order.getSourcePost());
        trackingRequestDTO.setStatus(status);

        return trackingRequestDTO;
    }

    public static OrderResponseDTO mapOrderEntityToResponseDTO(Order order) {
        return new OrderResponseDTO(order.getId(), order.getTimestamp(), order.getOrderStatus(), order.getUserId(), order.getUserName(), order.getUserLastName(),
                order.getUserAddress(), order.getUserPhoneNumber(), order.getProductName(), order.getProductWeight(), order.getSourcePost(),
                order.getDestinationPost(), order.getPrice(), order.getTypeOfService());
    }

    public static OrderDetailsDTO mapOrderEntityToDetailsDTO(Order order) {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO();
        orderDetailsDTO.setOrder(order);

        return orderDetailsDTO;
    }

}
