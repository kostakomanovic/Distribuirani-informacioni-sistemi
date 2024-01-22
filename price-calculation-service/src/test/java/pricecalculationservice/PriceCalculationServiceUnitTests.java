package pricecalculationservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import pricecalculationservice.dto.CalculatedPriceDTO;
import pricecalculationservice.dto.MileageDTO;
import pricecalculationservice.dto.OrderDetailsDTO;
import pricecalculationservice.dto.OrderDetailsDTOWithMileage;
import pricecalculationservice.proxies.PostServiceProxy;
import pricecalculationservice.services.PriceCalculationService;
import pricecalculationservice.utils.TypeOfService;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class PriceCalculationServiceUnitTests {
    @InjectMocks
    private PriceCalculationService priceCalculationService;

    @Mock
    PostServiceProxy postServiceProxy;

    @BeforeEach
    public void setUp() {
        Mono<MileageDTO> mileageDTOMono = Mono.fromSupplier(() -> new MileageDTO(200));
        when(postServiceProxy.getMileage(anyString(), anyString())).thenReturn(mileageDTOMono);
    }

    @Test
    public void calculateAndAddPriceBasedOnTypeOfService_returnsDouble_successful() {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456");
        OrderDetailsDTOWithMileage orderDetailsDTOWithMileage = new OrderDetailsDTOWithMileage(orderDetailsDTO, 200);

        CalculatedPriceDTO calculatedPriceDTO = new CalculatedPriceDTO();
        priceCalculationService.calculateAndAddPriceBasedOnTypeOfService(orderDetailsDTOWithMileage, calculatedPriceDTO);

        assertEquals(5, calculatedPriceDTO.getPrice());
    }

    @Test
    public void calculateAndAddPriceBasedOnWeight_returnsDouble_successful() {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(15, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456");
        OrderDetailsDTOWithMileage orderDetailsDTOWithMileage = new OrderDetailsDTOWithMileage(orderDetailsDTO, 200);

        CalculatedPriceDTO calculatedPriceDTO = new CalculatedPriceDTO();
        calculatedPriceDTO.setPrice(10);
        priceCalculationService.calculateAndAddPriceBasedOnWeight(orderDetailsDTOWithMileage, calculatedPriceDTO);

        assertEquals(20, calculatedPriceDTO.getPrice());
    }

    @Test
    public void calculateAndAddPriceBasedOnMileage_returnsDouble_successful() {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(15, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456");
        OrderDetailsDTOWithMileage orderDetailsDTOWithMileage = new OrderDetailsDTOWithMileage(orderDetailsDTO, 200);

        CalculatedPriceDTO calculatedPriceDTO = new CalculatedPriceDTO();
        calculatedPriceDTO.setPrice(10);
        priceCalculationService.calculateAndAddPriceBasedOnMileage(orderDetailsDTOWithMileage, calculatedPriceDTO);

        assertEquals(25, calculatedPriceDTO.getPrice());
    }


    @Test
    public void calculatePrice_returnsCorrectPrice_successful() {
        OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(15, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456");
        Mono<OrderDetailsDTO> orderDetailsDTOMono = Mono.fromSupplier(() -> orderDetailsDTO);

        StepVerifier.create(priceCalculationService.calculatePrice(orderDetailsDTOMono))
                .expectSubscription()
                .expectNextMatches(p -> p.getPrice() == 30)
                .verifyComplete();
    }
}
