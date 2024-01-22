package trackingservice.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import trackingservice.dtos.TrackingDTO;
import trackingservice.models.Tracking;
import trackingservice.repositories.TrackingRepository;
import trackingservice.utils.Mapper;

@Service
public class TrackingService {

    TrackingRepository repository;

    @Autowired
    public TrackingService(TrackingRepository repository) {
        this.repository = repository;
    }

    public Flux<Tracking> getTrackingsForOrder(String orderId) {
        return repository.findByOrderId(orderId);
    }

    public Mono<Tracking> saveNewTrackingRecord(Mono<TrackingDTO> trackingDTOMono) {
        return trackingDTOMono
                .map(Mapper::trackingDTOtoEntity)
                .flatMap(entity -> repository.save(entity));
    }
}
