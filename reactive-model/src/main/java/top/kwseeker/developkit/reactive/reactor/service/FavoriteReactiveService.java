package top.kwseeker.developkit.reactive.reactor.service;

import org.reactivestreams.Publisher;
import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.model.Book;
import top.kwseeker.developkit.reactive.model.BookMap;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FavoriteReactiveService {

    public Publisher<List<Book>> getDetails(List<String> favIds) {
        List<Book> books = favIds.stream().map(BookMap.MAP::get).collect(Collectors.toList());
        System.out.println("getDetails: ");
        books.forEach(book -> System.out.println(book.toString()));
        return subscriber -> subscriber.onNext(books);
    }
}
