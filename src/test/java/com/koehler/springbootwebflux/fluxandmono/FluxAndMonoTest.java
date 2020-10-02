package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.Optional;

public class FluxAndMonoTest {

    @Test
    public void printFlux() {
        Flux<String> stringFlux = Flux
                .just("Spri mtgesang", "Spring boot", "Reactive programing")
                .log();

        stringFlux
                .subscribe(System.out::println);
    }

    @Test
    public void printFluxWithError() {
        Flux<String> stringFlux = Flux
                .just("Spring", "Spring boot", "Reactive programing")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        stringFlux
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void printFluxWithMessageAfterError_shouldNotReceiveMessage() {
        Flux<String> stringFlux = Flux
                .just("Spring", "Spring boot", "Reactive programing")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("After error"))
                .log();

        stringFlux
                .subscribe(System.out::println, System.err::println);
    }

    @Test
    public void printFluxWithCompletedEvent() {
        Flux<String> stringFlux = Flux
                .just("Spring", "Spring boot", "Reactive programing")
                .log();

        stringFlux
                .subscribe(System.out::println, System.err::println, () -> System.out.println("Completed"));

        Arrays.stream(new String[]{"test1", "teste2"});

    }

    @Test
    public void testFlux() {

        Flux<String> stringFlux = Flux
                .just("Spring", "Spring boot", "Reactive programing")
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive programing")
                .verifyComplete();
    }

    @Test
    public void testFluxWithError() {

        Flux<String> stringFlux = Flux
                .just("Spring", "Spring boot", "Reactive programing")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNext("Spring")
                .expectNext("Spring boot")
                .expectNext("Reactive programing")
                .expectError(RuntimeException.class)
                .verify();
    }

    @Test
    public void testMono() {

        Mono<String> stringMono = Mono
                .just("Spring")
                .log();

        StepVerifier
                .create(stringMono)
                .expectNext("Spring")
                .verifyComplete();
    }

    @Test
    public void initializingFlux() {
        Flux.fromIterable(Arrays.asList("123", "123"));

    }
}
