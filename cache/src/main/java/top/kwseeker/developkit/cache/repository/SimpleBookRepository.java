package top.kwseeker.developkit.cache.repository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

@Component
//@CacheConfig(cacheNames = {"cache10s"})
public class SimpleBookRepository implements BookRepository {

    private static final Logger logger = LoggerFactory.getLogger(SimpleBookRepository.class);

    @Override
    @Cacheable(cacheNames = "cache10s", key = "'book-'+#isbn", sync = true)
    public Book getByIsbn(String isbn) {
        logger.info("db search ...");
        simulateSlowService();
        return new Book(isbn, "Some book");
    }

    @Override
    @CachePut(cacheNames = "cache10s", key = "'book-'+#isbn")
    public Book updateBookByIsbn(String isbn) throws InterruptedException {
        logger.info("db update ...");
        Thread.sleep(1000L);		//模拟更新耗时
        return new Book(isbn, "updated book");
    }

    @Override
    @CacheEvict(allEntries = true)
    public void deleteAllBook() throws InterruptedException {
        logger.info("db delete ...");
        Thread.sleep(1000L);
    }

    private void simulateSlowService() {
        try {
            long time = 3000L;
            Thread.sleep(time);
        } catch (InterruptedException e) {
            throw new IllegalStateException(e);
        }
    }

}
