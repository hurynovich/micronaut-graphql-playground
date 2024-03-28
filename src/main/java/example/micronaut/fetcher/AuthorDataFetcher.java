package example.micronaut.fetcher;

import example.micronaut.dao.DbRepository;
import example.micronaut.type.Author;
import example.micronaut.type.Book;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;

@Context
@Named("authorFetcher")
@AllArgsConstructor
public class AuthorDataFetcher implements DataFetcher<Author> {

  private final DbRepository dbRepository;

  @Override
  public Author get(DataFetchingEnvironment env) throws Exception {
    Book book = env.getSource();
    Author authorBook = book.getAuthor();
    return dbRepository.findAllAuthors()
        .stream()
        .filter(author -> author.getId().equals(authorBook.getId()))
        .findFirst()
        .orElse(null);
  }
}
