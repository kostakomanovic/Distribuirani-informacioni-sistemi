package orderingservice;

import orderingservice.dtos.*;
import orderingservice.models.Courier;
import orderingservice.models.Order;
import orderingservice.models.Post;
import orderingservice.proxies.PriceCalculationServiceProxy;
import orderingservice.proxies.TrackingServiceProxy;
import orderingservice.repositories.OrderingRepository;
import orderingservice.services.OrderingService;
import orderingservice.utils.OrderStatus;
import orderingservice.utils.TypeOfService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;


@ExtendWith(SpringExtension.class)
public class OrderingServiceUnitTests {

    @InjectMocks
    OrderingService orderingService;

    @Mock
    OrderingRepository orderingRepository;

    @Mock
    TrackingServiceProxy trackingServiceProxy;

    @Mock
    PriceCalculationServiceProxy priceCalculationServiceProxy;

    @BeforeEach
    public void setUp() {
        Order order = new Order("1234", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
                "maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
                new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11);
        when(orderingRepository.findById(anyString())).thenReturn(Mono.fromSupplier(() -> order));

        Order orderAfterSave = new Order("5555", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
                "maska za telefon", 0.8, new Post("123", "Posta Novi Sad", "Radnicka 22"),
                new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11);
        when(orderingRepository.save(any(Order.class))).thenReturn(Mono.fromSupplier(() -> orderAfterSave));

        List<Order> ordersList = new ArrayList<>();
        ordersList.add(new Order("1234", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
                "maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
                new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11));
        ordersList.add(new Order("7772", LocalDateTime.now(), OrderStatus.FINISHED, "222", "Marko", "Manic", "Majska 12", "333-335",
                "maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
                new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11));

        when(orderingRepository.findByUserId(anyString())).thenReturn(Flux.fromIterable(ordersList));

        List<TrackingResponseDTO> trackingResponseDTOList = new ArrayList<>();
        trackingResponseDTOList.add(new TrackingResponseDTO("777", LocalDateTime.now(),
                "1234", new Post("444", "Posta Kraljevo", "Ibarska bb"), new Courier("555", "Aleksa Santic")));
        trackingResponseDTOList.add(new TrackingResponseDTO("865", LocalDateTime.now(),
                "1234", new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("896", "Milos Peric")));

        when(trackingServiceProxy.getTrackingsForOrder(anyString())).thenReturn(Flux.fromIterable(trackingResponseDTOList));
        given(trackingServiceProxy.saveNewTackingRecord(any(TrackingRequestDTO.class))).willReturn(Mono.empty());

        PriceCalculationResponseDTO priceCalculationResponseDTO = new PriceCalculationResponseDTO(200);
        when(priceCalculationServiceProxy.calculatePrice(anyDouble(), any(TypeOfService.class), anyString(), anyString()))
                .thenReturn(Mono.fromSupplier(() -> priceCalculationResponseDTO));
    }

    @Test
    public void getOrderDetails_orderDetailsRetrieved_successful() {
        StepVerifier.create(orderingService.getOrderDetails("123"))
                .expectSubscription()
                .expectNextMatches(orderDetailsDTO -> orderDetailsDTO.getOrder().getId().equals("1234") && orderDetailsDTO.getTrackings().size() == 2)
                .verifyComplete();
    }

    @Test
    public void createOrder_orderCreated_successful() {
        OrderCreateDTO orderCreateDTO = new OrderCreateDTO("111", "Luka", "Lukic", "Majska 12", "212-243",
                "maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
                new Post("444", "Posta Kraljevo", "Ibarska bb"), TypeOfService.TODAY_FOR_TOMORROW_AFTER_11.toString());

        Mono<OrderCreateDTO> orderCreateDTOMono = Mono.fromSupplier(() -> orderCreateDTO);

        StepVerifier.create(orderingService.createOrder(orderCreateDTOMono))
                .expectSubscription()
                .expectNextMatches(order -> order.getId().equals("5555"))
                .verifyComplete();
    }

    @Test
    public void getOrdersByUser_ordersRetrieved_successful() {
        StepVerifier.create(orderingService.getOrdersByUser("22"))
                .expectSubscription()
                .expectNextMatches(o -> o.getId().equals("1234"))
                .expectNextMatches(o -> o.getId().equals("7772"))
                .verifyComplete();
    }

    @Test
    public void cancelOrder_orderCancelled_successful() {
        StepVerifier.create(orderingService.cancelOrder("22"))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void finishOrder_orderFinish_successful() {
        StepVerifier.create(orderingService.finishOrder("22"))
                .expectSubscription()
                .verifyComplete();
    }
}
