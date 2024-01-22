package pricecalculationservice.utils;

import pricecalculationservice.dto.OrderDetailsDTO;
import pricecalculationservice.dto.OrderDetailsDTOWithMileage;

public class Mapper {

    public static OrderDetailsDTOWithMileage extendOrderDetailsDto(OrderDetailsDTO orderDetailsDTO) {
        OrderDetailsDTOWithMileage orderDetailsDTOWithMileage = new OrderDetailsDTOWithMileage();
        orderDetailsDTOWithMileage.setOrderDetailsDTO(orderDetailsDTO);

        return orderDetailsDTOWithMileage;
    }
}
