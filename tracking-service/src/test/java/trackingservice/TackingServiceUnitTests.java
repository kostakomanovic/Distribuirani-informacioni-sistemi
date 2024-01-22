package trackingservice;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;
import trackingservice.dtos.TrackingDTO;
import trackingservice.models.Courier;
import trackingservice.models.Post;
import trackingservice.models.Tracking;
import trackingservice.repositories.TrackingRepository;
import trackingservice.services.TrackingService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class TackingServiceUnitTests {

    @InjectMocks
    private TrackingService trackingService;

    @Mock
    TrackingRepository trackingRepository;

    @BeforeEach
    public void setUp() {
        List<Tracking> trackingList = new ArrayList<>();
        trackingList.add(new Tracking("123", LocalDateTime.now(), "123456",
                new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("444", "Milos Andric")));
        trackingList.add(new Tracking("124", LocalDateTime.now(), "123456",
                new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("225", "Ivan Simic")));
        Flux<Tracking> trackingFlux = Flux.fromIterable(trackingList);

        when(trackingService.getTrackingsForOrder(any())).thenReturn(trackingFlux);

        Mono<Tracking> singleTrackingMono = Mono.fromSupplier(() -> new Tracking("124", LocalDateTime.now(), "123456",
                new Post("123", "Posta Novi Sad", "Radnicka 22"), new Courier("225", "Ivan Simic")));

        when(trackingRepository.save(any(Tracking.class))).thenReturn(singleTrackingMono);
    }

    @Test
    public void getTrackingsForOrder_returnTrackings_successful() {
        StepVerifier.create(trackingService.getTrackingsForOrder("123456"))
                .expectSubscription()
                .expectNextMatches(t -> t.getId().equals("123"))
                .expectNextMatches(t -> t.getId().equals("124"))
                .verifyComplete();
    }

    @Test
    public void saveNewTracking_returnsNewTracking_successful() {
        Mono<TrackingDTO> trackingDTOMono = Mono.fromSupplier(() ->
                new TrackingDTO("123456",
                        new Post("123", "Posta Novi Sad", "Radnicka 22"),
                        new Courier("225", "Ivan Simic")));

        StepVerifier.create(trackingService.saveNewTrackingRecord(trackingDTOMono))
                        .expectSubscription()
                        .expectNextMatches(t -> t.getId().equals("124"))
                        .verifyComplete();

    }


}
