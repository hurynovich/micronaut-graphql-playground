package example.micronaut.type;

import io.micronaut.core.annotation.Introspected;

import java.math.BigDecimal;

@Introspected
public class Book {

    private final String id;
    private final String name;
    private final int pageCount;
    private final Author author;
    private final BigDecimal price;

    public Book(String id, String name, int pageCount, Author author, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.pageCount = pageCount;
        this.author = author;
        this.price = price;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPageCount() {
        return pageCount;
    }

    public Author getAuthor() {
        return author;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
