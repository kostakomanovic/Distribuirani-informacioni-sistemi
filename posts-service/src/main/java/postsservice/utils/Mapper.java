package postsservice.utils;

import postsservice.dtos.PostDto;
import postsservice.models.Post;

public class Mapper {

    public static PostDto postEntityToDto(Post post) {
        return new PostDto(post.getId(), post.getName(), post.getAddress());
    }

    public static Post postDtoToEntity(PostDto postDto) {
        return new Post(null, postDto.getName(), postDto.getAddress());
    }
}
