package example.micronaut.fetcher;

import com.fasterxml.jackson.databind.ObjectMapper;
import example.micronaut.dao.DbRepository;
import example.micronaut.type.Author;
import example.micronaut.type.Book;
import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.context.annotation.Context;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;

import javax.validation.Valid;

@Context
@Named("authorFetcher")
@AllArgsConstructor
public class AuthorDataFetcher implements DataFetcher<Author> {

  private final DbRepository dbRepository;
  private final ObjectMapper objectMapper;

  @Override
  public Author get(DataFetchingEnvironment env) {

    Book book = env.getSource();
    env.getSource();
    env.getContext();

    Author authorBook = book.getAuthor();
    return dbRepository.findAllAuthors()
        .stream()
        .filter(author -> author.getId().equals(authorBook.getId()))
        .findFirst()
        .orElse(null);
  }
}
