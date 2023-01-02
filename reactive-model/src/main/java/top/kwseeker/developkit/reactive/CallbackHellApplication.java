package top.kwseeker.developkit.reactive;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import top.kwseeker.developkit.reactive.callback.hell.HellService;

public class CallbackHellApplication {

    public static void main(String[] args) {
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext("top.kwseeker.developkit.reactive");
        HellService hellService = context.getBean(HellService.class);
        hellService.showFavorites(10001L);
    }
}
