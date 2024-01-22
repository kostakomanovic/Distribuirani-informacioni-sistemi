package orderingservice.proxies;

import orderingservice.dtos.PriceCalculationResponseDTO;
import orderingservice.utils.TypeOfService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactivefeign.spring.config.ReactiveFeignClient;
import reactor.core.publisher.Mono;

@ReactiveFeignClient(name = "price-calculation-service", url = "${price.calculation.service.url}")
public interface PriceCalculationServiceProxy {

    @GetMapping("calculate-price")
    public Mono<PriceCalculationResponseDTO> calculatePrice(@RequestParam double weight,
                                                            @RequestParam TypeOfService typeOfService,
                                                            @RequestParam String sourcePostId,
                                                            @RequestParam String destinationPostId);
}
