package com.example.demo;

import com.example.demo.dtos.CourierDetailsDTO;
import com.example.demo.models.Courier;
import com.example.demo.repositories.CouriersRepository;
import com.example.demo.services.CouriersService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CouriersServiceUnitTests {

    @InjectMocks
    private CouriersService couriersService;

    @Mock
    CouriersRepository couriersRepository;

    @BeforeEach
    public void setUp() {
        List<Courier> couriers = new ArrayList<>();
        couriers.add(new Courier("123", "Luka", "Lukic", "123", new ArrayList<>(), 2));
        couriers.add(new Courier("124", "Maja", "Majic", "124", new ArrayList<>(), 5));
        couriers.add(new Courier("125", "Ivan", "Ivic", "125", new ArrayList<>(), 6));

        Flux<Courier> couriersFlux = Flux.fromIterable(couriers);
        when(couriersRepository.findAll()).thenReturn(couriersFlux);

        Courier singleCourier = new Courier("888", "Luka", "Lukic", "123", new ArrayList<>(), 2);
        Mono<Courier> singleCourierMono = Mono.fromSupplier(() -> singleCourier);
        when(couriersRepository.findById(anyString())).thenReturn(singleCourierMono);

        when(couriersRepository.save(any(Courier.class))).thenReturn(singleCourierMono);

        given(couriersRepository.deleteById(anyString())).willReturn(Mono.empty());
    }

    @Test
    public void findAllCouriers_couriersRetrieved_successful() {
        StepVerifier.create(couriersService.findAllCouriers())
                .expectSubscription()
                .expectNextMatches(c -> c.getId().equals("123"))
                .expectNextMatches(c -> c.getId().equals("124"))
                .expectNextMatches(c -> c.getId().equals("125"))
                .verifyComplete();
    }

    @Test
    public void getCourierById_courierRetrieved_successful() {
        StepVerifier.create(couriersService.getCourierById("888"))
                .expectSubscription()
                .expectNextMatches(c -> c.getId().equals("888"))
                .verifyComplete();
    }

    @Test
    public void insertCourier_courierSaved_successful() {
        CourierDetailsDTO courierDetailsDTO = new CourierDetailsDTO(null, "Name", "Last Name", "123", new ArrayList<>(), 2);
        Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() -> courierDetailsDTO);

        StepVerifier.create(couriersService.insertCourier(courierDetailsDTOMono))
                .expectSubscription()
                .expectNextMatches(c -> c.getId().equals("888"))
                .verifyComplete();
    }

    @Test
    public void updateCourier_courierUpdated_successful() {
        CourierDetailsDTO courierDetailsDTO = new CourierDetailsDTO(null, "Name", "Last Name", "123", new ArrayList<>(), 2);
        Mono<CourierDetailsDTO> courierDetailsDTOMono = Mono.fromSupplier(() -> courierDetailsDTO);

        StepVerifier.create(couriersService.updateCourier("888", courierDetailsDTOMono))
                .expectSubscription()
                .expectNextMatches(c -> c.getId().equals("888"))
                .verifyComplete();
    }

    @Test
    public void deleteCourier_courierDeleted_successful() {
        StepVerifier.create(couriersService.deleteCourier("888"))
                .expectSubscription()
                .verifyComplete();
    }

    @Test
    public void removeCommentForCourier_commentDeleted_successful() {
        StepVerifier.create(couriersService.removeCommentForCourier("888", "111"))
                .expectSubscription()
                .verifyComplete();
    }

}
