package postsservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import postsservice.dtos.PostDto;
import postsservice.repositories.PostsRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import postsservice.utils.Mapper;

@Service
public class PostService {

    PostsRepository repository;

    @Autowired
    public PostService(PostsRepository repository) {
        this.repository = repository;
    }

    public Flux<PostDto> getAllPosts() {
        return repository.findAll().map(Mapper::postEntityToDto);
    }

    public Mono<PostDto> getPostById(String id) {
        return repository.findById(id)
                .map(Mapper::postEntityToDto);
    }

    public Mono<PostDto> insertPost(Mono<PostDto> postDtoMono) {
        return postDtoMono
                .map(Mapper::postDtoToEntity)
                .flatMap(repository::save)
                .map(Mapper::postEntityToDto);
    }

    public Mono<PostDto> updatePost(String id, Mono<PostDto> postDto) {
        return repository
                .findById(id)
                .flatMap(p -> postDto
                                .map(Mapper::postDtoToEntity)
                                .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save)
                .map(Mapper::postEntityToDto);
    }

    public Mono<Void> deletePost(String id) {
        return this.repository.deleteById(id);
    }
}
