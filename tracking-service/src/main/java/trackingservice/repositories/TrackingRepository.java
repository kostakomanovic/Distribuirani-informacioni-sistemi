package trackingservice.repositories;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import trackingservice.models.Tracking;

@Repository
public interface TrackingRepository extends ReactiveCrudRepository<Tracking, String> {

    Flux<Tracking> findByOrderId(String orderId);

}
