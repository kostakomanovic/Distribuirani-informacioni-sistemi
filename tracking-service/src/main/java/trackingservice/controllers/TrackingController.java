package trackingservice.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import trackingservice.dtos.TrackingDTO;
import trackingservice.models.Tracking;
import trackingservice.services.TrackingService;

@RestController
@RequestMapping("tracking")
public class TrackingController {

    private TrackingService trackingService;

    @Autowired
    public TrackingController(TrackingService trackingService) {
        this.trackingService = trackingService;
    }

    @GetMapping("{orderId}")
    public Flux<Tracking> getTrackingsForOrder(@PathVariable String orderId) {
        return trackingService.getTrackingsForOrder(orderId);
    }

    @PostMapping
    public Mono<Tracking> saveNewTrackingRecord(@RequestBody Mono<TrackingDTO> trackingDTO) {
        return trackingService.saveNewTrackingRecord(trackingDTO);
    }

}
