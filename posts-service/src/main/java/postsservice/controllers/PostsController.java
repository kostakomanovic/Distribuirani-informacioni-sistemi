package postsservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import postsservice.dtos.PostDto;
import postsservice.service.PostService;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("posts")
public class PostsController {

    PostService service;

    @Autowired
    public PostsController(PostService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<PostDto> getAllPosts() {
        return service.getAllPosts();
    }

    @GetMapping("{id}")
    public Mono<PostDto> getPostById(@PathVariable String id) {
        return service.getPostById(id);
    }

    @PostMapping
    public Mono<PostDto> insertPost(@RequestBody Mono<PostDto> postDtoMono) {
        return this.service.insertPost(postDtoMono);
    }

    @PutMapping("{id}")
    public Mono<PostDto> updatePost(@PathVariable String id, @RequestBody Mono<PostDto> postDtoMono) {
        return service.updatePost(id, postDtoMono);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deletePost(@PathVariable String id) {
        return this.service.deletePost(id);
    }

}
