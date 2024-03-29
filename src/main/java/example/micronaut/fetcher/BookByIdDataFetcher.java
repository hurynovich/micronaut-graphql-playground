package example.micronaut.fetcher;

import example.micronaut.dao.DbRepository;
import example.micronaut.type.Book;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;

@Context
@Named("bookByIdFetcher")
@AllArgsConstructor
public class BookByIdDataFetcher implements DataFetcher<Book> {

  private final DbRepository dbRepository;

  @Override
  public Book get(DataFetchingEnvironment env) {
    String bookId = env.getArgument("id"); // <3>
    return dbRepository.findAllBooks() // <4>
        .stream()
        .filter(book -> book.getId().equals(bookId))
        .findFirst()
        .orElse(null);
  }
}
