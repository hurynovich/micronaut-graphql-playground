package example.micronaut.config;

import example.micronaut.graphql.annotations.AnnotatedMethodTypeWiringBuilder;
import graphql.GraphQL;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLSchema;
import graphql.schema.idl.RuntimeWiring;
import graphql.schema.idl.SchemaGenerator;
import graphql.schema.idl.SchemaParser;
import graphql.schema.idl.TypeDefinitionRegistry;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.annotation.Factory;
import io.micronaut.core.io.ResourceResolver;
import jakarta.inject.Named;
import lombok.extern.slf4j.Slf4j;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.random.RandomGenerator;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Slf4j
@Factory
public class UtilsFactory {

  @Context
  public RandomGenerator newSecureRandom() {
      return new SecureRandom();
    }

}
