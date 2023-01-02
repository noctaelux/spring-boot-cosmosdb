package com.example.springbootcosmosdb.repositories;

import com.azure.spring.data.cosmos.repository.ReactiveCosmosRepository;
import com.example.springbootcosmosdb.models.User;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface UserRepository extends ReactiveCosmosRepository<User,String> {
    Flux<User> findByFirstName(String firstName);
}
