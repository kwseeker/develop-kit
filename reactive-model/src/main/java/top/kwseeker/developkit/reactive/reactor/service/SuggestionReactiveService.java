package top.kwseeker.developkit.reactive.reactor.service;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.model.Book;

import java.util.Arrays;
import java.util.List;

@Service
public class SuggestionReactiveService {

    public Publisher<List<Book>> getSuggestions() {
        List<Book> suggestionBooks = Arrays.asList(new Book("世界通史"),
                            new Book("时间简史"),
                            new Book("乌合之众"),
                            new Book("怪诞行为学"),
                            new Book("经济学原理"));
        return subscriber -> subscriber.onNext(suggestionBooks);
    }
}
