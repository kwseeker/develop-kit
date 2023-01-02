package top.kwseeker.developkit.reactive;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.developkit.reactive.reactor.reactive.ReactiveService;

public class ReactiveApplication {

    public static void main(String[] args) throws InterruptedException {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.developkit.reactive");
        ReactiveService hellService = context.getBean(ReactiveService.class);
        hellService.showFavorites(10001L);

        Thread.currentThread().join(2000L);
    }
}
