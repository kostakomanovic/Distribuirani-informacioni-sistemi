package pricecalculationservice.proxies;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pricecalculationservice.dto.MileageDTO;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "posts-service", url = "${post.service.url}")
public interface PostServiceProxy {

    @GetMapping("mileage/from/{sourcePostId}/to/{destinationPostId}")
    public Mono<MileageDTO> getMileage(@PathVariable String sourcePostId, @PathVariable String destinationPostId);
}
