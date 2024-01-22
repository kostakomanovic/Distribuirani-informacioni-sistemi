package com.example.demo.services;

import com.example.demo.dtos.CommentDTO;
import com.example.demo.dtos.CourierBasicInfoDTO;
import com.example.demo.dtos.CourierDetailsDTO;
import com.example.demo.repositories.CouriersRepository;
import com.example.demo.utils.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class CouriersService {

    private CouriersRepository repository;

    @Autowired
    public CouriersService(CouriersRepository repository) {
        this.repository = repository;
    }

    public Flux<CourierBasicInfoDTO> findAllCouriers() {
        return repository
                .findAll()
                .map(Mapper::entityToBasicInfoDto);
    }

    public Mono<CourierDetailsDTO> getCourierById(String id) {
        return repository.findById(id)
                .map(Mapper::entityToDetailsDto);
    }

    public Mono<CourierDetailsDTO> insertCourier(Mono<CourierDetailsDTO> postDtoMono) {
        return postDtoMono
                .map(Mapper::detailsDtoToEntity)
                .flatMap(repository::save)
                .map(Mapper::entityToDetailsDto);
    }

    public Mono<CourierDetailsDTO> updateCourier(String id, Mono<CourierDetailsDTO> postDto) {
        return repository
                .findById(id)
                .flatMap(p -> postDto
                        .map(Mapper::detailsDtoToEntity)
                        .doOnNext(e -> e.setId(id)))
                .flatMap(repository::save)
                .map(Mapper::entityToDetailsDto);
    }

    public Mono<Void> deleteCourier(String id) {
        return this.repository.deleteById(id);
    }

    public Mono<CourierDetailsDTO> saveCommentForCourier(String courierId, Mono<CommentDTO> commentDTOMono) {
        return repository
                .findById(courierId)
                .flatMap(p -> commentDTOMono
                        .map(Mapper::commentDtoToEntity)
                        .doOnNext(e -> e.setId(UUID.randomUUID().toString()))
                        .doOnNext(e -> e.setTimestamp(LocalDateTime.now()))
                        .doOnNext(e -> p.getComments().add(e))
                        .map(e -> p))
                .flatMap(repository::save)
                .map(Mapper::entityToDetailsDto);
    }

    public Mono<Void> removeCommentForCourier(String courierId, String commentId) {
        return repository
                .findById(courierId)
                .flatMap(c -> Mono.fromSupplier(() -> c)
                                  .doOnNext(courier -> courier.setComments(courier.getComments()
                                                              .stream()
                                                              .filter(comment -> !comment.getId().equals(commentId))
                                                              .collect(Collectors.toList()))))
                .flatMap(repository::save)
                .flatMap(c -> Mono.empty());
    }
}
