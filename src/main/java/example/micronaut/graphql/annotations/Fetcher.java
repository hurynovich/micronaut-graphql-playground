package example.micronaut.graphql.annotations;

import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.EntryPoint;
import io.micronaut.http.annotation.HttpMethodMapping;
import io.micronaut.scheduling.annotation.Scheduled;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.lang.annotation.RetentionPolicy.SOURCE;

@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Executable
@Repeatable(Fetcher.Container.class)
public @interface Fetcher {
  String type();
  String field();

  @Documented
  @Inherited
  @Target({ElementType.METHOD})
  @Retention(RUNTIME)
  @interface Container {
    Fetcher[] value();
  }
}

