package example.micronaut.graphql.annotations;

import io.micronaut.context.annotation.Executable;
import io.micronaut.core.annotation.EntryPoint;
import io.micronaut.http.annotation.HttpMethodMapping;

import java.lang.annotation.*;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Retention(RUNTIME)
@Target({ElementType.METHOD})
@Inherited
@Executable
public @interface Fetcher {
  String type();
  String field();
}

