package example.micronaut.fetcher;


import example.micronaut.graphql.annotations.Attribute;
import example.micronaut.graphql.annotations.Fetcher;
import example.micronaut.graphql.annotations.GraphqlWiring;
import example.micronaut.graphql.annotations.Source;
import example.micronaut.type.Author;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.context.annotation.Context;
import lombok.AllArgsConstructor;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.random.RandomGenerator;


@Context
@AllArgsConstructor
@GraphqlWiring
public class RandomNameFetcher {
  private final RandomGenerator rnd;

  @Fetcher(type = "Query", field = "generateFakeAuthor")
  public Author generateFakeAuthor() {
    return new Author();
  }

  @Fetcher(type = "FakeAuthor", field = "firstName")
  @Fetcher(type = "FakeAuthor", field = "lastName")
  public String getRandomName(
      @Attribute @NotNull @Min(2) Integer min,
      @Attribute @NotNull @Max(64) Integer max,
      @Source Author author,
      DataFetchingEnvironment env
  ) {
    StringBuilder sb = new StringBuilder();
    int len = rnd.nextInt(min, max + 1);
    sb.append((char) rnd.nextInt('A', 'Z' + 1));
    for (int i = 1; i < len; i++) {
      sb.append((char) rnd.nextInt('a', 'z' + 1));
    }

    return sb.toString();
  }

}
