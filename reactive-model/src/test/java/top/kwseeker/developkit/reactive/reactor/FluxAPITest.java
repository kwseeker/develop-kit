package top.kwseeker.developkit.reactive.reactor;

import org.junit.Test;
import reactor.core.publisher.Flux;

import java.util.Random;

public class FluxAPITest {

    @Test
    public void test1() {
        Flux.generate(
                () -> 0,
                (value, sink) -> {
                    if (value == 3) {
                        sink.complete();
                    } else {
                        sink.next("value = " + value);
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
