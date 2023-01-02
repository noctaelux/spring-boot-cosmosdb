package com.example.springbootcosmosdb.runners;

import com.example.springbootcosmosdb.models.User;
import com.example.springbootcosmosdb.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Optional;

@Component
public class WingtiptoysdataApplication implements CommandLineRunner {

    private static final Logger LOGGER = LoggerFactory.getLogger(WingtiptoysdataApplication.class);

    @Autowired
    private UserRepository userRepository;

    public void run(String... var1) {
        userRepository.deleteAll().block();
        LOGGER.info("Deleted all data in container.");

        final User testUser = new User("1234", "Pepe", "Pecas", "Entre ríos y cerros.");

        // Save the User class to Azure Cosmos DB database.
        final Mono<User> saveUserMono = userRepository.save(testUser);

        final Flux<User> firstNameUserFlux = userRepository.findByFirstName("Pepe");

        //  Nothing happens until we subscribe to these Monos.
        //  findById will not return the user as user is not present.
        final Mono<User> findByIdMono = userRepository.findById(testUser.getId());
        final User findByIdUser = findByIdMono.block();
        Assert.isNull(findByIdUser, "User must be null");

        final User savedUser = saveUserMono.block();
        Assert.state(savedUser != null, "Saved user must not be null");
        Assert.state(savedUser.getFirstName().equals(testUser.getFirstName()), "Saved user first name doesn't match");

        firstNameUserFlux.collectList().block();

        final Optional<User> optionalUserResult = userRepository.findById(testUser.getId()).blockOptional();
        Assert.isTrue(optionalUserResult.isPresent(), "Cannot find user.");

        final User result = optionalUserResult.get();
        Assert.state(result.getFirstName().equals(testUser.getFirstName()), "query result firstName doesn't match!");
        Assert.state(result.getLastName().equals(testUser.getLastName()), "query result lastName doesn't match!");

        LOGGER.info("findOne in User collection get result: {}", result);
    }

}
