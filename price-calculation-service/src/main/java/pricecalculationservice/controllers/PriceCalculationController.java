package pricecalculationservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pricecalculationservice.dto.CalculatedPriceDTO;
import pricecalculationservice.dto.OrderDetailsDTO;
import pricecalculationservice.services.PriceCalculationService;
import pricecalculationservice.utils.TypeOfService;
import reactor.core.publisher.Mono;

import java.util.logging.Logger;

@RestController
public class PriceCalculationController {
    private static final Logger LOG = Logger.getLogger(PriceCalculationController.class.getName());

    private PriceCalculationService service;

    @Autowired
    public PriceCalculationController(PriceCalculationService service) {
        this.service = service;
    }

    @GetMapping("/calculate-price")
    public Mono<CalculatedPriceDTO> calculatePrice(@RequestParam double weight, @RequestParam String typeOfService,
                                                   @RequestParam String sourcePostId, @RequestParam String destinationPostId) {
        return service.calculatePrice(Mono.fromSupplier(() ->
                new OrderDetailsDTO(weight, TypeOfService.valueOf(typeOfService), sourcePostId, destinationPostId)));
    }
}
