package pricecalculationservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import pricecalculationservice.controllers.PriceCalculationController;
import pricecalculationservice.dto.CalculatedPriceDTO;
import pricecalculationservice.dto.OrderDetailsDTO;
import pricecalculationservice.services.PriceCalculationService;
import pricecalculationservice.utils.TypeOfService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(PriceCalculationController.class)
class PriceCalculationServiceApplicationIntegrationTests {

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	PriceCalculationService priceCalculationService;

	@Test
	public void calculatePriceMissingQueryParamTest() {
		Mono<CalculatedPriceDTO> calculatedPriceDTOMono = Mono.fromSupplier(() -> new CalculatedPriceDTO(200));
		Mono<OrderDetailsDTO> orderDetailsDTOMono = Mono.fromSupplier(() -> new OrderDetailsDTO(200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456"));

		when(priceCalculationService.calculatePrice(orderDetailsDTOMono)).thenReturn(calculatedPriceDTOMono);

		webTestClient.get()
				.uri(uriBuilder ->
						uriBuilder.path("/calculate-price")
								.queryParam("weight", 200)
								.queryParam("typeOfService", TypeOfService.TODAY_FOR_TOMORROW_AFTER_11.toString())
								.queryParam("sourcePostId", "123")
								.build())
				.exchange()
				.expectStatus().is4xxClientError();
	}

	@Test
	public void calculatePriceTest() {
		CalculatedPriceDTO calculatedPriceDTO =  new CalculatedPriceDTO(200);
		Mono<CalculatedPriceDTO> calculatedPriceDTOMono = Mono.fromSupplier(() -> calculatedPriceDTO);

		OrderDetailsDTO orderDetailsDTO = new OrderDetailsDTO(200, TypeOfService.TODAY_FOR_TOMORROW_AFTER_11, "123", "456");

		Mono<OrderDetailsDTO> orderDetailsDTOMono = Mono.fromSupplier(() -> orderDetailsDTO);

		when(priceCalculationService.calculatePrice(orderDetailsDTOMono)).thenReturn(calculatedPriceDTOMono);

		Flux<CalculatedPriceDTO> responseBody = webTestClient.get()
				.uri(uriBuilder ->
						uriBuilder.path("/calculate-price")
								.queryParam("weight", 200)
								.queryParam("typeOfService", TypeOfService.TODAY_FOR_TOMORROW_AFTER_11.toString())
								.queryParam("sourcePostId", "123")
								.queryParam("destinationPostId", "456")
								.build())
				.exchange()
				.expectStatus().isOk()
				.returnResult(CalculatedPriceDTO.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
//				.expectNextMatches(dto -> dto.getPrice() == 200)
				.verifyComplete();
	}

}
