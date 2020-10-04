package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;
import reactor.util.retry.Retry;

import java.time.Duration;

public class FluxAndMonoErrorTest {

    @Test
    public void flux_withError() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                //.expectError(RuntimeException.class)
                .expectErrorMessage("Error")
                .verify();

    }

    @Test
    public void flux_withErrorHandling() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorResume(throwable -> {
                    System.out.println("Exception " + throwable);
                    return Flux.just("Default", "Default2");
                })
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C", "Default", "Default2")
                //.expectError(RuntimeException.class)
                //.expectErrorMessage("Error")
                .verifyComplete();

    }

    @Test
    public void flux_withErrorHandling_onErrorReturn() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorReturn("Default")
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("Default")
                .verifyComplete();

    }

    @Test
    public void flux_withErrorHandling_onErrorMap() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(throwable -> new NullPointerException(throwable.getMessage()))
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectError(NullPointerException.class)
                .verify();

    }

    @Test
    public void flux_withErrorHandling_onErrorMap_withRetry() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(throwable -> new NullPointerException(throwable.getMessage()))
                .retry(2)
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(NullPointerException.class)
                .verify();

    }

    @Test
    public void flux_withErrorHandling_onErrorMap_withRetryBackoff() {

        Flux<String> stringFlux = Flux.just("A", "B", "C")
                .concatWith(Flux.error(new RuntimeException("Error")))
                .concatWith(Flux.just("D"))
                .onErrorMap(throwable -> new NullPointerException(throwable.getMessage()))
                //.retryBackoff(2, Duration.ofSeconds(5))
                .retryWhen(Retry.backoff(2, Duration.ofSeconds(5)))
                .log();

        StepVerifier.create(stringFlux)
                .expectSubscription()
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectNext("A", "B", "C")
                .expectError(IllegalStateException.class)
                .verify();

    }
}
