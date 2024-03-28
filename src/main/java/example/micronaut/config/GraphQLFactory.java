package example.micronaut.config;

import graphql.GraphQL;
import graphql.scalar.GraphqlBigDecimalCoercing;
import graphql.schema.DataFetcher;
import graphql.schema.GraphQLScalarType;
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
import java.util.Optional;

import static graphql.schema.idl.TypeRuntimeWiring.newTypeWiring;

@Slf4j
@Factory
public class GraphQLFactory {

  @Context
  public GraphQL graphQL(
      ResourceResolver resourceResolver,
      @Named("bookByIdFetcher") DataFetcher<?> bookByIdFetcher,
      @Named("authorFetcher") DataFetcher<?> authorFetcher
  ) {
    SchemaParser schemaParser = new SchemaParser();

    TypeDefinitionRegistry typeRegistry = new TypeDefinitionRegistry();
    Optional<InputStream> graphqlSchema = resourceResolver.getResourceAsStream("classpath:schema.graphqls");

    if (graphqlSchema.isPresent()) {
      typeRegistry.merge(schemaParser.parse(new BufferedReader(new InputStreamReader(graphqlSchema.get()))));

      RuntimeWiring runtimeWiring = RuntimeWiring.newRuntimeWiring()
          .type(
              newTypeWiring("Query").dataFetcher("bookById", bookByIdFetcher)
          )
          .type(
              newTypeWiring("Book").dataFetcher("author", authorFetcher)
          )
          .scalar(
              GraphQLScalarType.newScalar()
                  .name("Float")
                  .description("BigDecimal implementation")
                  .coercing(new GraphqlBigDecimalCoercing())
                  .build()
          )
          .build();

      SchemaGenerator schemaGenerator = new SchemaGenerator();
      GraphQLSchema graphQLSchema = schemaGenerator.makeExecutableSchema(typeRegistry, runtimeWiring); // <8>

      return GraphQL.newGraphQL(graphQLSchema).build(); // <9>

    } else {
      log.debug("No GraphQL services found, returning empty schema");
      return new GraphQL.Builder(GraphQLSchema.newSchema().build()).build();
    }
  }
}
