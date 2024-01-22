package postsservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import postsservice.dtos.PostDto;
import postsservice.models.Post;
import postsservice.repositories.PostsRepository;
import postsservice.service.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class PostServiceUnitTests {

    @InjectMocks
    private PostService postService;

    @Mock
    PostsRepository postsRepository;

    @BeforeEach
    public void setUp() {
        List<Post> posts = new ArrayList<>();
        posts.add(new Post("123", "Posta Novi Sad Radnicka", "Radnicka 12"));
        posts.add(new Post("443", "Posta Novi Sad Safarikova", "Safarikova 22a"));
        posts.add(new Post("624", "Posta Kraljevo", "Moravska 2"));
        Flux<Post> postFlux = Flux.fromIterable(posts);

        when(postsRepository.findAll()).thenReturn(postFlux);

        Post singlePost = new Post("123", "Posta Novi Sad Radnicka", "Radnicka 12");

        when(postsRepository.findById(anyString())).thenReturn(Mono.fromSupplier(() -> singlePost));

        when(postsRepository.save(any(Post.class))).thenReturn(Mono.fromSupplier(() -> singlePost));

        given(postsRepository.deleteById(anyString())).willReturn(Mono.empty());
    }

    @Test
    public void getAllPosts_returnPostsFlux_successful() {
        StepVerifier.create(postService.getAllPosts())
                .expectSubscription()
                .expectNextMatches(dto -> dto.getId().equals("123"))
                .expectNextMatches(dto -> dto.getId().equals("443"))
                .expectNextMatches(dto -> dto.getId().equals("624"))
                .verifyComplete();
    }

    @Test
    public void getPostById_returnPostMono_successful() {
        StepVerifier.create(postService.getPostById("123"))
                .expectSubscription()
                .expectNextMatches(dto -> dto.getId().equals("123"))
                .verifyComplete();
    }

    @Test
    public void insertPost_returnPostMono_successful() {
        Mono<PostDto> postDtoMono = Mono.fromSupplier(() -> new PostDto("123", "Test", "TestAddr"));

        StepVerifier.create(postService.insertPost(postDtoMono))
                .expectSubscription()
                .expectNextMatches(dto -> dto.getName().equals("Posta Novi Sad Radnicka"))
                .verifyComplete();
    }

    @Test
    public void updatePost_returnPostMono_successful() {
        Mono<PostDto> postDtoMono = Mono.fromSupplier(() -> new PostDto("123", "Test", "TestAddr"));

        StepVerifier.create(postService.updatePost("123", postDtoMono))
                .expectSubscription()
                .expectNextMatches(dto -> dto.getName().equals("Posta Novi Sad Radnicka"))
                .verifyComplete();
    }

    @Test
    public void deletePost_returnNothing_successful() {
        StepVerifier.create(postService.deletePost("123"))
                .expectSubscription()
                .verifyComplete();
    }
}
