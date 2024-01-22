package postsservice;

import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import postsservice.controllers.PostsController;
import postsservice.dtos.PostDto;
import postsservice.service.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@RunWith(SpringRunner.class)
@WebFluxTest(PostsController.class)
class PostsServiceApplicationIntegrationTests {

	@Autowired
	private WebTestClient webTestClient;
	@MockBean
	private PostService postService;

	@Test
	public void getPostsTest() {
		List<PostDto> dtos = new ArrayList<>();
		dtos.add(new PostDto("123", "Posta Novi Sad Radnicka", "Radnicka 12"));
		dtos.add(new PostDto("443", "Posta Novi Sad Safarikova", "Safarikova 22a"));
		dtos.add(new PostDto("624", "Posta Kraljevo", "Moravska 2"));

		Flux<PostDto> postDtoFlux = Flux.fromIterable(dtos);

		when(postService.getAllPosts()).thenReturn(postDtoFlux);

		Flux<PostDto> responseBody = webTestClient.get()
				.uri("/posts")
				.exchange()
				.expectStatus().isOk()
				.returnResult(PostDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNext(new PostDto("123", "Posta Novi Sad Radnicka", "Radnicka 12"))
				.expectNext(new PostDto("443", "Posta Novi Sad Safarikova", "Safarikova 22a"))
				.expectNext(new PostDto("624", "Posta Kraljevo", "Moravska 2"))
				.verifyComplete();
	}
	@Test
	public void getSinglePostTest() {
		Mono<PostDto> postDtoMono = Mono.fromSupplier(() ->
				new PostDto("123", "Posta Raska", "Srpskih heroja 24"));
		when(postService.getPostById(any())).thenReturn(postDtoMono);

		Flux<PostDto> responseBody = webTestClient.get()
				.uri("/posts/1")
				.exchange()
				.expectStatus().isOk()
				.returnResult(PostDto.class)
				.getResponseBody();

		StepVerifier.create(responseBody)
				.expectSubscription()
				.expectNextMatches(p -> p.getId().equals("123"))
				.verifyComplete();

	}

	@Test
	public void insertPostTest() {
		Mono<PostDto> postDtoMono = Mono.fromSupplier(() ->
				new PostDto("123", "Posta Raska", "Srpskih heroja 24")
				);

		when(postService.insertPost(postDtoMono)).thenReturn(postDtoMono);

		webTestClient.post().uri("/posts")
				.body(Mono.fromSupplier(() -> postDtoMono), PostDto.class)
				.exchange()
				.expectStatus().isOk();
	}

	@Test
	public void updatePostTest() {
		Mono<PostDto> postDtoMono = Mono.fromSupplier(() ->
				new PostDto("123", "Posta Raska", "Srpskih heroja 24"));

		when(postService.updatePost("123", postDtoMono)).thenReturn(postDtoMono);

		webTestClient.put()
				.uri("/posts/2")
				.body(Mono.fromSupplier(() -> postDtoMono), PostDto.class)
				.exchange()
				.expectStatus()
				.isOk();
	}

	@Test
	public void deletePostsTest() {
		given(postService.deletePost(any())).willReturn(Mono.empty());

		webTestClient.delete()
				.uri("/posts/2")
				.exchange()
				.expectStatus().isOk();
	}

}