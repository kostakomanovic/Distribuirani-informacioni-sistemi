package orderingservice.services;

import orderingservice.dtos.OrderCreateDTO;
import orderingservice.dtos.OrderDetailsDTO;
import orderingservice.dtos.OrderResponseDTO;
import orderingservice.models.Order;
import orderingservice.proxies.PriceCalculationServiceProxy;
import orderingservice.proxies.TrackingServiceProxy;
import orderingservice.repositories.OrderingRepository;
import orderingservice.utils.Mapper;
import orderingservice.utils.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class OrderingService {

    private final OrderingRepository repository;
    private final PriceCalculationServiceProxy priceCalculationServiceProxy;
    private final TrackingServiceProxy trackingServiceProxy;


    @Autowired
    public OrderingService(OrderingRepository repository, PriceCalculationServiceProxy priceCalculationServiceProxy,
                           TrackingServiceProxy trackingServiceProxy) {
        this.repository = repository;
        this.priceCalculationServiceProxy = priceCalculationServiceProxy;
        this.trackingServiceProxy = trackingServiceProxy;
    }

    public Mono<OrderDetailsDTO> getOrderDetails(String orderId) {
        return repository
                .findById(orderId)
                .flatMap(entity -> trackingServiceProxy
                        .getTrackingsForOrder(orderId)
                        .collectList()
                        .map(trackings -> new OrderDetailsDTO(entity, trackings))
                );
    }

    public Mono<Order> createOrder(Mono<OrderCreateDTO> orderDTO) {
            return orderDTO
                    .map(Mapper::mapOrderCreationDtoToEntity)
                    .flatMap(entity -> priceCalculationServiceProxy
                            .calculatePrice(entity.getProductWeight(),
                                    entity.getTypeOfService(),
                                    entity.getSourcePost().getId(),
                                    entity.getDestinationPost().getId())
                            .doOnNext(priceDTO -> entity.setPrice(priceDTO.getPrice()))
                            .map(priceDto -> entity)
                    )
                    .flatMap(repository::save)
                    .flatMap(entity -> trackingServiceProxy
                            .saveNewTackingRecord(Mapper.orderEntityToTrackingRequestDTO(entity, "Arrived in the post"))
                            .then(Mono.fromSupplier(() -> entity))
                    );
    }

    public Flux<OrderResponseDTO> getOrdersByUser(String userId) {
        return repository
                .findByUserId(userId)
                .map(Mapper::mapOrderEntityToResponseDTO);
    }

    public Mono<Void> cancelOrder(String orderId) {
        return repository
                .findById(orderId)
                .doOnNext(entity -> entity.setOrderStatus(OrderStatus.CANCELLED))
                .flatMap(repository::save)
                .doOnNext(entity -> trackingServiceProxy.saveNewTackingRecord(Mapper.orderEntityToTrackingRequestDTO(entity, "Order is cancelled")))
                .thenEmpty(Mono.empty());
    }

    public Mono<Void> finishOrder(String orderId) {
        return repository
                .findById(orderId)
                .doOnNext(entity -> entity.setOrderStatus(OrderStatus.FINISHED))
                .flatMap(repository::save)
                .doOnNext(entity -> trackingServiceProxy.saveNewTackingRecord(Mapper.orderEntityToTrackingRequestDTO(entity, "Order arrived at destination")))
                .thenEmpty(Mono.empty());
    }
}
