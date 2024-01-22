package orderingservice.proxies;

import orderingservice.dtos.TrackingRequestDTO;
import orderingservice.dtos.TrackingResponseDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "tracking-service", url = "${tracking.service.url}")
public interface TrackingServiceProxy {

    @PostMapping("tracking")
    public Mono<Void> saveNewTackingRecord(@RequestBody TrackingRequestDTO trackingRequestDTOMono);

    @GetMapping("tracking/{orderId}")
    public Flux<TrackingResponseDTO> getTrackingsForOrder(@PathVariable String orderId);

}
