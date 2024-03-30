package example.micronaut.graphql.annotations;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.TypeRuntimeWiring;
import io.micronaut.context.ExecutionHandleLocator;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.processor.ExecutableMethodProcessor;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.core.type.Argument;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.ExecutableMethod;
import io.micronaut.inject.MethodExecutionHandle;
import jakarta.inject.Named;
import lombok.AllArgsConstructor;

import javax.validation.ConstraintViolationException;
import java.lang.annotation.Annotation;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

@Context
@AllArgsConstructor
public class AnnotatedMethodTypeWiringBuilder implements ExecutableMethodProcessor<GraphqlController> {
  private final ExecutionHandleLocator executionHandleLocator;
  private final List<TypeRuntimeWiring> typeWirings = new LinkedList<>();

  @Override
  public void process(BeanDefinition<?> beanDefinition, ExecutableMethod<?, ?> method) {
    Optional<AnnotationValue<Fetcher>> fetcherMetaOpt = method.findAnnotation(Fetcher.class);
    if (fetcherMetaOpt.isEmpty()) {
      return;
    }
    AnnotationValue<Fetcher> fetcherAnnotation = fetcherMetaOpt.get();
    String graphqlTypeName = fetcherAnnotation.stringValue("type").orElseThrow();
    String graphqlFieldName = fetcherAnnotation.stringValue("field").orElseThrow();

    MethodExecutionHandle<?, Object> handle =
        executionHandleLocator.createExecutionHandle(beanDefinition, (ExecutableMethod<Object, ?>) method);

    typeWirings.add(
        TypeRuntimeWiring.newTypeWiring(graphqlTypeName)
            .dataFetcher(graphqlFieldName, createDataFetcher(handle))
            .build()
    );
  }

  public List<TypeRuntimeWiring> getTypeWirings() {
    return Collections.unmodifiableList(typeWirings);
  }

  private Function<DataFetchingEnvironment, Object[]> createAllArgumentsProvider(MethodExecutionHandle<?, Object> handle) {
    List<Function<DataFetchingEnvironment, Object>> argProviders = Stream.of(handle.getArguments())
        .map(this::createArgumentProvider)
        .toList();
    return env -> argProviders.stream()
        .map(argProvider -> argProvider.apply(env))
        .toArray();
  }

  private Function<DataFetchingEnvironment, Object> createArgumentProvider(Argument<?> argument) {
    checkAnnotationsCollision(argument);
    if (argument.isAnnotationPresent(Attribute.class)) {
      final String argName = argument.getAnnotation(Attribute.class).stringValue().orElse(argument.getName());
      return env -> env.getArgument(argName);
    } else if (argument.isAnnotationPresent(Source.class)) {
      return env -> env.getSource();
    } else if (argument.isAnnotationPresent(Root.class)) {
      return env -> env.getRoot();
    } else if (argument.isAssignableFrom(DataFetchingEnvironment.class)) {
      return env -> env;
    }

    throw new IllegalStateException("Parameter '" + argument.getName() +
        "' is part of graphql Fetcher method but not annotated with any annotation neither belongs to known classes.");
  }

  private void checkAnnotationsCollision(Argument<?> argument) {
    List<Class<? extends Annotation>> incompatible = List.of(Attribute.class, Source.class, Root.class).stream()
        .filter(argument::isAnnotationPresent)
        .toList();
    if (incompatible.size() > 1) {
      throw new IllegalStateException("Parameter '" + argument.getName()
          + "' is annotated with incompatible annotations " + incompatible);
    }
  }

  private DataFetcher createDataFetcher(MethodExecutionHandle<?, Object> handle) {
    final Function<DataFetchingEnvironment, Object[]> argsProvider = createAllArgumentsProvider(handle);
    return env -> {
      try {
        return handle.invoke(argsProvider.apply(env));
      } catch (ConstraintViolationException ex) {
        return convertException(ex);
      }
    };
  }

  private Object convertException(ConstraintViolationException ex) {
    //TODO: implement
    return ex;
  }
}
