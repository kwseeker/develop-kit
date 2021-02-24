package top.kwseeker.developkit.cache.repository;

import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class SimpleBookRepositoryTest {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBookRepositoryTest.class);

    @Resource
    private BookRepository bookRepository;

    @Test
    void getByIsbn() {
        logger.info(".... Fetching books");
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
        logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        logger.info(".... search more time");
        Book book = bookRepository.getByIsbn("isbn-1234");
        logger.info("isbn-1234 -->" + book);
        logger.info("isbn-4567 -->" + bookRepository.getByIsbn("isbn-4567"));
        logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
    }

    @Test
    void syncGetByIsbn() throws InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(4);
        for (int i = 0; i < 4; i++) {
            es.submit(() -> {
                logger.info(".... Fetching books");
                logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
                logger.info(".... search more time");
                Book book = bookRepository.getByIsbn("isbn-1234");
                logger.info("isbn-1234 -->" + book);
                logger.info("isbn-1234 -->" + bookRepository.getByIsbn("isbn-1234"));
            });
        }
        Thread.currentThread().join(100000);
    }

    @Test
    void updateBookByIsbn() throws InterruptedException {
        logger.info(".... update book by isbn");
        bookRepository.updateBookByIsbn("isbn-1234");
        logger.info(".... update done");
    }

    @Test
    void deleteBookByIsbn() throws InterruptedException {
        logger.info(".... delete book by isbn");
        bookRepository.deleteAllBook();
        logger.info(".... delete done");
    }
}