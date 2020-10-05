package com.koehler.springbootwebflux.fluxandmono;

import org.junit.jupiter.api.Test;
import org.reactivestreams.Subscription;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

public class FluxAndMonoBackPressureTest {

    @Test
    public void backPressureTest() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        StepVerifier.create(integerFlux)
                .expectSubscription()
                .thenRequest(1)
                .expectNext(1)
                .thenRequest(1)
                .expectNext(2)
                .thenCancel()
                .verify();
    }

    @Test
    public void backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                integer -> System.out.println("Element: " + integer),
                throwable -> System.out.println("Error: " + throwable),
                () -> System.out.println("Completed"),
                subscription -> subscription.request(10));
    }

    @Test
    public void backPressure_cancel() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                integer -> System.out.println("Element: " + integer),
                throwable -> System.out.println("Error: " + throwable),
                () -> System.out.println("Completed"),
                Subscription::cancel);
    }

    @Test
    public void customize_backPressure() {
        Flux<Integer> integerFlux = Flux.range(1, 10).log();

        integerFlux.subscribe(
                new BaseSubscriber<Integer>() {
                    @Override
                    protected void hookOnNext(Integer value) {
                        request(5);
                        System.out.println("Element: " + value);
                        if (value == 4) {
                            cancel();
                        }
                    }
                }
        );
    }
}
