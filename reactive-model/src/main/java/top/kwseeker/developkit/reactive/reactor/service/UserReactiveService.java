package top.kwseeker.developkit.reactive.reactor.service;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;

@Service
public class UserReactiveService {

    public Flux<List<String>> getFavorites(Long userId) {
        //Publisher<List<String>> publisher = getFavoriteArray(userId);
        return Flux.just(getFavoriteList(userId));
    }

    public List<String> getFavoriteList(Long userId) {
        return Arrays.asList("资本论", "资治通鉴", "鲁迅文集");
    }

    public Publisher<List<String>> getFavoriteArray(Long userId) {
        List<String> suggestionBooks = Arrays.asList("资本论", "资治通鉴", "鲁迅文集");
        return subscriber -> subscriber.onNext(suggestionBooks);
    }
}
