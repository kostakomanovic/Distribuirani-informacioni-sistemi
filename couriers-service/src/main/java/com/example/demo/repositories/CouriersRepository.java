package com.example.demo.repositories;

import com.example.demo.models.Courier;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouriersRepository extends ReactiveCrudRepository<Courier, String> {
}
