package com.example.demo;

import com.example.demo.controllers.CouriersController;
import com.example.demo.dtos.CommentDTO;
import com.example.demo.dtos.CourierBasicInfoDTO;
import com.example.demo.dtos.CourierDetailsDTO;
import com.example.demo.models.Comment;
import com.example.demo.services.CouriersService;
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
@WebFluxTest(CouriersController.class)
class CouriersServiceApplicationIntegrationTests {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private CouriersService couriersService;

	@Test
	public void getCouriersTest() {
		List<CourierBasicInfoDTO> dtos = new ArrayList<>();
		dtos.add(new CourierBasicInfoDTO("123", "Luka", "Lukic"));
		dtos.add(new CourierBasicInfoDTO("124", "Maja", "Majic"));
		dtos.add(new CourierBasicInfoDTO("125", "Ivan", "Ivic"));

		Flux<CourierBasicInfoDTO> courierBasicInfoDTOFlux = Flux.fromIterable(dtos);

		when(couriersService.findAllCouriers()).thenReturn(courierBasicInfoDTOFlux);

		Flux<CourierBasicInfoDTO> responseBody = webTestClient.get()
				.uri("/couriers")
				.exchange()
				.expectStatus().isOk()
				.returnResult(CourierBasicInfoDTO.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new CourierBasicInfoDTO("123", "Luka", "Lukic"))
				.expectNext(new CourierBasicInfoDTO("124", "Maja", "Majic"))
				.expectNext(new CourierBasicInfoDTO("125", "Ivan", "Ivic"))
				.verifyComplete();
	}

	@Test
	public void getSingleCourierTest() {
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("123",  LocalDateTime.now(), "Test", true));

		Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() ->
				new CourierDetailsDTO("123", "Luka", "Lukic",
						"123456", comments,5));
		when(couriersService.getCourierById(any())).thenReturn(courierDetailsDTOMono);

		Flux<CourierDetailsDTO> responseBody = webTestClient.get()
				.uri("/couriers/1")
				.exchange()
				.expectStatus().isOk()
				.returnResult(CourierDetailsDTO.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(c -> c.getId().equals("123"))
				.verifyComplete();

	}

	@Test
	public void insertCourierTest() {
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("123",  LocalDateTime.now(), "Test", true));

		Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() ->
				new CourierDetailsDTO("123", "Luka",
						"Lukic", "123456",
						comments, 5));

		when(couriersService.insertCourier(courierDetailsDTOMono)).thenReturn(courierDetailsDTOMono);

		webTestClient.post().uri("/couriers")
				.body(Mono.fromSupplier(() -> courierDetailsDTOMono), CourierDetailsDTO.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void updateCourierTest() {
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("123",  LocalDateTime.now(),"Test", true));

		Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() ->
				new CourierDetailsDTO("123", "Luka","Lukic", "123456", comments, 5));

		when(couriersService.updateCourier("123", courierDetailsDTOMono)).thenReturn(courierDetailsDTOMono);

		webTestClient.put()
				.uri("/couriers/2")
				.body(Mono.fromSupplier(() -> courierDetailsDTOMono), CourierDetailsDTO.class)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void deleteCourierTest() {
		given(couriersService.deleteCourier(any())).willReturn(Mono.empty());

		webTestClient.delete()
				.uri("/couriers/2")
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void addCommentForCourier() {
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("123",  LocalDateTime.now(),"Test", true));

		Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() ->
				new CourierDetailsDTO("123", "Luka","Lukic", "123456", comments, 5));

		Mono<CommentDTO> commentDTOMono = Mono.fromSupplier(() -> new CommentDTO("testCom", true));

		when(couriersService.saveCommentForCourier("123", commentDTOMono)).thenReturn(courierDetailsDTOMono);

		webTestClient.post()
				.uri("/couriers/2/comment")
				.body(Mono.fromSupplier(() -> courierDetailsDTOMono), CourierDetailsDTO.class)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void remoteCommentForCourier() {
		List<Comment> comments = new ArrayList<>();
		comments.add(new Comment("123",  LocalDateTime.now(),"Test", true));
		Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() ->
				new CourierDetailsDTO("123", "Luka","Lukic", "123456", comments, 5));

		given(couriersService.removeCommentForCourier(any(), any())).willReturn(Mono.empty());

		webTestClient.delete()
				.uri("/couriers/2/comment/3")
				.exchange()
				.expectStatus()
				.isOk();
	}


}
