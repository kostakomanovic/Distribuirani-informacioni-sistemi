package com.example.demo.controllers;

import com.example.demo.dtos.CommentDTO;
import com.example.demo.dtos.CourierBasicInfoDTO;
import com.example.demo.dtos.CourierDetailsDTO;
import com.example.demo.services.CouriersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("couriers")
public class CouriersController {

    private CouriersService service;

    @Autowired
    public CouriersController(CouriersService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<CourierBasicInfoDTO> getAllCouriers() {
        return service.findAllCouriers();
    }

    @GetMapping("{id}")
    public Mono<CourierDetailsDTO> getCourierById(@PathVariable String id) {
        return service.getCourierById(id);
    }

    @PostMapping
    public Mono<CourierDetailsDTO> insertCourier(@RequestBody Mono<CourierDetailsDTO>courierDetailsDTOMono) {
        return this.service.insertCourier(courierDetailsDTOMono);
    }

    @PutMapping("{id}")
    public Mono<CourierDetailsDTO> updateCourier(@PathVariable String id, @RequestBody Mono<CourierDetailsDTO> courierDetailsDTOMono) {
        return service.updateCourier(id, courierDetailsDTOMono);
    }

    @DeleteMapping("{id}")
    public Mono<Void> deleteCourier(@PathVariable String id) {
        return this.service.deleteCourier(id);
    }

    @PostMapping("{courierId}/comment")
    public Mono<CourierDetailsDTO> addCommentForCourier(@PathVariable String courierId, @RequestBody Mono<CommentDTO> commentDTOMono) {
        return this.service.saveCommentForCourier(courierId, commentDTOMono);
    }

    @DeleteMapping("{courierId}/comment/{commentId}")
    public Mono<Void> removeCommentForCourier(@PathVariable String courierId, @PathVariable String commentId) {
        return this.service.removeCommentForCourier(courierId, commentId);
    }

}
