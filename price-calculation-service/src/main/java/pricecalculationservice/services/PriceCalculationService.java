package pricecalculationservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pricecalculationservice.dto.CalculatedPriceDTO;
import pricecalculationservice.dto.OrderDetailsDTO;
import pricecalculationservice.dto.OrderDetailsDTOWithMileage;
import pricecalculationservice.proxies.PostServiceProxy;
import pricecalculationservice.utils.Mapper;
import pricecalculationservice.utils.TypeOfService;
import reactor.core.publisher.Mono;

@Service
public class PriceCalculationService {

    private final PostServiceProxy postServiceProxy;

    @Autowired
    public PriceCalculationService(PostServiceProxy postServiceProxy) {
        this.postServiceProxy = postServiceProxy;
    }

    public Mono<CalculatedPriceDTO> calculatePrice(Mono<OrderDetailsDTO> detailsDTOMono) {
        return detailsDTOMono
                .map(Mapper::extendOrderDetailsDto)
                .flatMap(details -> Mono.fromSupplier(CalculatedPriceDTO::new)
                        .doOnNext(dto -> calculateAndAddPriceBasedOnTypeOfService(details, dto))
                        .doOnNext(dto -> calculateAndAddPriceBasedOnWeight(details, dto))
                        .flatMap(dto -> postServiceProxy
                                .getMileage(details.getOrderDetailsDTO().getSourcePostId(), details.getOrderDetailsDTO().getDestinationPostId())
                                .doOnNext(mileageDTO -> details.setMileage(mileageDTO.getMileage()))
                                .map(mileageDTO -> dto)
                        )
                        .doOnNext(dto -> calculateAndAddPriceBasedOnMileage(details, dto))
                );
    }

    public CalculatedPriceDTO calculateAndAddPriceBasedOnTypeOfService(OrderDetailsDTOWithMileage orderDetailsDTO, CalculatedPriceDTO calculatedPriceDTO) {
        if (orderDetailsDTO.getOrderDetailsDTO().getTypeOfService() == TypeOfService.TODAY_FOR_TODAY) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 10);
        } else if (orderDetailsDTO.getOrderDetailsDTO().getTypeOfService() == TypeOfService.TODAY_FOR_TOMORROW_UNTIL_11) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 8);
        } else if (orderDetailsDTO.getOrderDetailsDTO().getTypeOfService() == TypeOfService.TODAY_FOR_TOMORROW_AFTER_11) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 5);
        }

        return calculatedPriceDTO;
    }

    public CalculatedPriceDTO calculateAndAddPriceBasedOnWeight(OrderDetailsDTOWithMileage orderDetailsDTO, CalculatedPriceDTO calculatedPriceDTO) {
        if (orderDetailsDTO.getOrderDetailsDTO().getWeight() < 5) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 2);
        } else if (5 <= orderDetailsDTO.getOrderDetailsDTO().getWeight() && orderDetailsDTO.getOrderDetailsDTO().getWeight() < 10) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 5);
        } else if (10 <= orderDetailsDTO.getOrderDetailsDTO().getWeight() && orderDetailsDTO.getOrderDetailsDTO().getWeight() < 15) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 7);
        } else if (15 <= orderDetailsDTO.getOrderDetailsDTO().getWeight()) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 10);
        }

        return calculatedPriceDTO;
    }

    public CalculatedPriceDTO calculateAndAddPriceBasedOnMileage(OrderDetailsDTOWithMileage orderDetailsDTO, CalculatedPriceDTO calculatedPriceDTO) {
        if (orderDetailsDTO.getMileage() < 20) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 4);
        } else if (20 <= orderDetailsDTO.getMileage() && orderDetailsDTO.getMileage() < 50) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 8);
        } else if (50 <= orderDetailsDTO.getMileage() && orderDetailsDTO.getMileage() < 100) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 12);
        } else if (100 <= orderDetailsDTO.getMileage()) {
            calculatedPriceDTO.setPrice(calculatedPriceDTO.getPrice() + 15);
        }

        return calculatedPriceDTO;
    }

}
