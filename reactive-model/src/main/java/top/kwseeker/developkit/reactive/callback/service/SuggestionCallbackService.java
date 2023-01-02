package top.kwseeker.developkit.reactive.callback.service;

import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.callback.Callback;
import top.kwseeker.developkit.reactive.model.Book;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Service
public class SuggestionCallbackService {

    public void getSuggestions(Callback<List<Book>> callback) {
        try {
            List<Book> suggestedBooks = getSuggestions();
            callback.onSuccess(suggestedBooks);
        } catch (Exception e) {
            callback.onError(e);
        }
    }

    private List<Book> getSuggestions() {
        Random random = new Random();
        if (random.nextInt(5) == 4) {
            throw new RuntimeException("Exception Occur!");
        }
        return Arrays.asList(new Book("世界通史"),
                new Book("时间简史"),
                new Book("乌合之众"),
                new Book("怪诞行为学"),
                new Book("经济学原理"));
    }
}
