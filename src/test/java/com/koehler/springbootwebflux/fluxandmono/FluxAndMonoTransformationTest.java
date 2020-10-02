package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.Arrays;
import java.util.List;

public class FluxAndMonoTransformationTest {

    List<String> stringFlux = Arrays.asList("A", "B", "C", "D", "E", "F");

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

        return Arrays.asList(s, "newValue");
    }
}
