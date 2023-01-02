package top.kwseeker.developkit.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Random;

public class FluxLambdaTest {

    @Test
    public void test1() {
        Random random = new Random();
        Flux.just(1, 2, 3)
                .map(value -> {
                    if (random.nextInt(4) == 3) {
                        throw new RuntimeException("Exception Occurred!");
                    }
                    return value + 1;
                })
                .subscribe(
                        Utils::println,
                        Utils::println,
                        () -> {
                            Utils.println("Subscription is completed!");
                        });
    }
}
