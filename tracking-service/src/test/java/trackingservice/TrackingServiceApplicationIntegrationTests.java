package trackingservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import trackingservice.controllers.TrackingController;
import trackingservice.dtos.TrackingDTO;
import trackingservice.models.Courier;
import trackingservice.models.Post;
import trackingservice.models.Tracking;
import trackingservice.services.TrackingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(TrackingController.class)
class TrackingServiceApplicationIntegrationTests {

	@Autowired
	WebTestClient webTestClient;

	@MockBean
	TrackingService trackingService;

	@Test
	public void getTrackingsForOrderTest() {
		List<Tracking> trackings = new ArrayList<>();
		trackings.add(new Tracking("123", LocalDateTime.now(), "123456",
				new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("444", "Milos Andric")));
		trackings.add(new Tracking("124", LocalDateTime.now(), "444456",
				new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("225", "Ivan Simic")));
		Flux<Tracking> trackingFlux = Flux.fromIterable(trackings);

		when(trackingService.getTrackingsForOrder(any())).thenReturn(trackingFlux);

		Flux<Tracking> responseBody = webTestClient.get()
				.uri("/tracking/22")
				.exchange()
				.expectStatus().isOk()
				.returnResult(Tracking.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new Tracking("123", LocalDateTime.now(), "123456",
						new Post("123", "Posta Novi Sad",
								"Radnicka 22"), new Courier("444", "Milos Andric")))
				.expectNext(new Tracking("124", LocalDateTime.now(), "444456",
						new Post("123", "Posta Novi Sad",
								"Radnicka 22"), new Courier("225", "Ivan Simic")))
				.verifyComplete();
	}

	@Test
	public void saveNewTrackingTest() {
		Mono<TrackingDTO> trackingDTOMono = Mono.fromSupplier(() ->
				new TrackingDTO("123456",
						new Post("123", "Posta Novi Sad", "Radnicka 22"),
						new Courier("225", "Ivan Simic")));


		Mono<Tracking> trackingMono = Mono.fromSupplier(() -> new Tracking("123", LocalDateTime.now(), "123456",
						new Post("123", "Posta Novi Sad",
								"Radnicka 22"), new Courier("444", "Milos Andric")));

		when(trackingService.saveNewTrackingRecord(trackingDTOMono)).thenReturn(trackingMono);

		webTestClient.post().uri("/tracking")
				.body(Mono.fromSupplier(() -> trackingDTOMono), TrackingDTO.class)
				.exchange()
				.expectStatus().isOk();
	}
}
