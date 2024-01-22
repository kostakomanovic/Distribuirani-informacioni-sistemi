package postsservice.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import postsservice.models.Post;

@Repository
public interface PostsRepository extends ReactiveCrudRepository<Post, String> {
}
