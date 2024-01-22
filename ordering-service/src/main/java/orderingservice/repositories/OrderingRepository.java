package orderingservice.repositories;

import orderingservice.models.Order;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface OrderingRepository extends ReactiveCrudRepository<Order, String> {

    Flux<Order> findByUserId(String userId);

}
