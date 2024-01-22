package postsservice.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import postsservice.dtos.MileageDTO;
import reactor.core.publisher.Mono;

import java.util.Random;

@RestController
public class UtilsController {

    @GetMapping("mileage/from/{sourcePostId}/to/{destinationPostId}")
    public Mono<MileageDTO> getMileage(@PathVariable String sourcePostId, @PathVariable String destinationPostId) {
        return Mono.fromSupplier(() -> {
            Random random = new Random();
            return new MileageDTO(random.nextInt(200) + 1);
        });
    }
}
