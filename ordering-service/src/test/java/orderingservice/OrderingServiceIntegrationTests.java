package orderingservice;

import orderingservice.controllers.OrderingController;
import orderingservice.dtos.OrderCreateDTO;
import orderingservice.dtos.OrderDetailsDTO;
import orderingservice.dtos.OrderResponseDTO;
import orderingservice.dtos.TrackingResponseDTO;
import orderingservice.models.Courier;
import orderingservice.models.Order;
import orderingservice.models.Post;
import orderingservice.services.OrderingService;
import orderingservice.utils.OrderStatus;
import orderingservice.utils.TypeOfService;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(OrderingController.class)
class OrderingServiceIntegrationTests {

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	OrderingService orderingService;

	@Test
	public void getOrdersByUserTest() {
		List<OrderResponseDTO> orders = new ArrayList<>();
		orders.add(new OrderResponseDTO("1234", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
				"maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
				new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11));
		orders.add(new OrderResponseDTO("5573", LocalDateTime.now(), OrderStatus.FINISHED, "111", "Luka", "Lukic", "Majska 12", "212-243",
				"baterija za iphone", 0.5, new Post("123", "Posta Novi Sad", "Radnicka 22"),
				new Post("444", "Posta Kraljevo", "Ibarska bb"), 3000, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11));

		Flux<OrderResponseDTO> orderResponseDTOFlux = Flux.fromIterable(orders);

		when(orderingService.getOrdersByUser(any())).thenReturn(orderResponseDTOFlux);

		Flux<OrderResponseDTO> responseBody = webTestClient.get()
				.uri("/order/user/111")
				.exchange()
				.expectStatus().isOk()
				.returnResult(OrderResponseDTO.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(dto -> dto.getId().equals("1234"))
				.expectNextMatches(dto -> dto.getId().equals("5573"))
				.verifyComplete();
	}

	@Test
	public void createOrderTest() {
		OrderCreateDTO orderCreateDTO = new OrderCreateDTO("111", "Luka", "Lukic", "Majska 12", "212-243",
				"maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
				new Post("444", "Posta Kraljevo", "Ibarska bb"), TypeOfService.TODAY_FOR_TOMORROW_AFTER_11.toString());

		Mono<OrderCreateDTO> orderCreateDTOMono = Mono.fromSupplier(() -> orderCreateDTO);

		Order order = new Order("1234", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
				"maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
				new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11);
		Mono<Order> orderMono = Mono.fromSupplier(() -> order);

		when(orderingService.createOrder(orderCreateDTOMono)).thenReturn(orderMono);

		webTestClient.post().uri("/order")
				.body(Mono.fromSupplier(() -> orderCreateDTOMono), OrderCreateDTO.class)
				.exchange()
				.expectStatus().isOk();

	}

	@Test
	public void getOrderDetailsTest() {
		Order order = new Order("1234", LocalDateTime.now(), OrderStatus.IN_PROGRESS, "111", "Luka", "Lukic", "Majska 12", "212-243",
				"maska za telefon", 0.4, new Post("123", "Posta Novi Sad", "Radnicka 22"),
				new Post("444", "Posta Kraljevo", "Ibarska bb"), 200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11);

		List<TrackingResponseDTO> trackingResponseDTOList = new ArrayList<>();
		trackingResponseDTOList.add(new TrackingResponseDTO("777", LocalDateTime.now(),
				"1234", new Post("444", "Posta Kraljevo", "Ibarska bb"), new Courier("555", "Aleksa Santic")));
		trackingResponseDTOList.add(new TrackingResponseDTO("865", LocalDateTime.now(),
				"1234", new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("896", "Milos Peric")));

		OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(order, trackingResponseDTOList);

		Mono<OrderDetailsDTO> orderDetailsDTOMono = Mono.fromSupplier(() -> orderDetailsDTO);

		when(orderingService.getOrderDetails(any())).thenReturn(orderDetailsDTOMono);

		Flux<OrderDetailsDTO> responseBody = webTestClient.get()
				.uri("/order/1234")
				.exchange()
				.expectStatus().isOk()
				.returnResult(OrderDetailsDTO.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(o -> o.getOrder().getId().equals("1234"))
				.verifyComplete();

	}

	@Test
	public void cancelOrderTest() {
		given(orderingService.cancelOrder(any())).willReturn(Mono.empty());

		webTestClient.patch()
				.uri("/order/1234/cancel")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void finishOrderTest() {
		given(orderingService.finishOrder(any())).willReturn(Mono.empty());

		webTestClient.patch()
				.uri("/order/1234/finish")
				.exchange()
				.expectStatus().isOk();
	}

}
