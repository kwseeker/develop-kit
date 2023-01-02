package top.kwseeker.developkit.reactive.callback.service;

import org.springframework.stereotype.Service;
import top.kwseeker.developkit.reactive.callback.Callback;
import top.kwseeker.developkit.reactive.model.Book;
import top.kwseeker.developkit.reactive.model.BookMap;

@Service
public class FavoriteCallbackService {

    public void getDetails(String favId, Callback<Book> callback) {
        try {
            Book book = BookMap.MAP.get(favId);
            callback.onSuccess(book);
        } catch (Throwable e) {
            callback.onError(e);
        }
    }
}
