package example.micronaut.dao;

import example.micronaut.type.Author;
import example.micronaut.type.Book;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Singleton;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Context
public class DbRepository {

    private static final List<Book> books = Arrays.asList( // <1>
            new Book("book-1", "Harry Potter and the Philosopher's Stone", 223, new Author("author-1", "Joanne", "Rowling"), new BigDecimal("19.99")),
            new Book("book-2", "Moby Dick", 635, new Author("author-2", "Herman", "Melville"), new BigDecimal("16.50")),
            new Book("book-3", "Interview with the vampire", 371, new Author("author-3", "Anne", "Rice"), new BigDecimal("10.00"))
    );

    public List<Book> findAllBooks() {
        return books;
    }

    public List<Author> findAllAuthors() {
        return books.stream()
                .map(Book::getAuthor)
                .collect(Collectors.toList());
    }
}
