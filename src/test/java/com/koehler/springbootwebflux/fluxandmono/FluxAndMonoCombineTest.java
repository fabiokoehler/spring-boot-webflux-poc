package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;

public class FluxAndMonoCombineTest {

    @Test
    public void combineUsingMerge() {

        Flux<String> flux1 = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();
    }

    @Test
    public void combineUsingMerge_withDelay() {
        Flux<String> flux1 = Flux.fromIterable(Arrays.asList("A", "B", "C")).delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> mergeFlux = Flux.merge(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                //.expectNext("A", "B", "C", "D", "E", "F")
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat() {

        Flux<String> flux1 = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> mergeFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("A", "B", "C", "D", "E", "F")
                .verifyComplete();

    }

    @Test
    public void combineUsingConcat_withDelay() {
        Flux<String> flux1 = Flux.fromIterable(Arrays.asList("A", "B", "C")).delayElements(Duration.ofSeconds(1));
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> mergeFlux = Flux.concat(flux1, flux2);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                //.expectNext("A", "B", "C", "D", "E", "F")
                .expectNextCount(6)
                .verifyComplete();

    }

    @Test
    public void combineUsingZip() {

        Flux<String> flux1 = Flux.fromIterable(Arrays.asList("A", "B", "C"));
        Flux<String> flux2 = Flux.fromIterable(Arrays.asList("D", "E", "F"));

        Flux<String> mergeFlux = Flux.zip(flux1, flux2, String::concat);

        StepVerifier.create(mergeFlux.log())
                .expectSubscription()
                .expectNext("AD", "BE", "CF")
                .verifyComplete();

    }

}