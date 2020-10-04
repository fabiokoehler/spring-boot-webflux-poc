package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

import static reactor.core.scheduler.Schedulers.parallel;

public class FluxAndMonoTransformationTest {

    List<String> stringFlux = Arrays.asList("A", "B", "C", "D", "E", "F");

    @Test
    public void fluxWithParallel() {
        Flux<String> stringFlux = Flux
                .fromIterable(this.stringFlux)
                .window(2)
                .flatMap(stringFlux1 -> stringFlux1.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void fluxWithParallelSequential() {
        Flux<String> stringFlux = Flux
                .fromIterable(this.stringFlux)
                .window(2)
                .flatMapSequential(stringFlux1 -> stringFlux1.map(this::convertToList).subscribeOn(parallel()))
                .flatMap(Flux::fromIterable)
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }

    @Test
    public void flux() {
        Flux<String> stringFlux = Flux
                .fromIterable(this.stringFlux)
                .flatMap(s -> Flux.fromIterable(convertToList(s)))
                .log();

        StepVerifier
                .create(stringFlux)
                .expectNextCount(12)
                .verifyComplete();
    }


    private List<String> convertToList(String s) {

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Arrays.asList(s, s + " - newValue");
    }
}
