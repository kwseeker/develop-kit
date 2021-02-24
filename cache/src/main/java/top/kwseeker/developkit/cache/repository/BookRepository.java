package top.kwseeker.developkit.cache.repository;

public interface BookRepository {

    Book getByIsbn(String isbn);

    Book updateBookByIsbn(String isbn) throws InterruptedException;

    void deleteAllBook() throws InterruptedException;
}
