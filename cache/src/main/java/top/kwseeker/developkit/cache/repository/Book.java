package top.kwseeker.developkit.cache.repository;

public class Book {

    //使用JDK序列化器需要实现Serializable接口
    //private static final long serialVersionUID = -9104737521522400280L;

    private String isbn;
    private String title;

    //使用GenericJackson2JsonRedisSerializer时用到无参构造器
    public Book() {}

    public Book(String isbn, String title) {
        this.isbn = isbn;
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public String toString() {
        return "Book{" + "isbn='" + isbn + '\'' + ", title='" + title + '\'' + '}';
    }
}
