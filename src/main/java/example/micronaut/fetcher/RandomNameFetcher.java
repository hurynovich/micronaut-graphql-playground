package example.micronaut.fetcher;


import example.micronaut.graphql.annotations.EnvAttribute;
import example.micronaut.graphql.annotations.Fetcher;
import example.micronaut.graphql.annotations.GraphqlController;
import graphql.schema.DataFetchingEnvironment;
import io.micronaut.context.annotation.Context;

import java.util.UUID;

@Context
@GraphqlController
public class RandomNameFetcher {
  {
    System.out.println("======================>");
  }
  @Fetcher(type = "Author", field = "firstName")
  String getRandomName(
      @EnvAttribute Integer min,
      @EnvAttribute Integer max,
      @example.micronaut.graphql.annotations.EnvSource DataFetchingEnvironment env
  ) {
    env.
    return UUID.randomUUID().toString();
  }


}
