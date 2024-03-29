package example.micronaut.graphql.annotations;

import graphql.schema.DataFetcher;
import graphql.schema.DataFetchingEnvironment;
import graphql.schema.idl.TypeRuntimeWiring;
import io.micronaut.context.ExecutionHandleLocator;
import io.micronaut.context.annotation.Context;
import io.micronaut.context.processor.ExecutableMethodProcessor;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.BeanDefinition;
import io.micronaut.inject.ExecutableMethod;
import io.micronaut.inject.MethodExecutionHandle;
import lombok.AllArgsConstructor;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

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
    String graphqlTypeName = fetcherAnnotation.stringValue("type").orElseThrow(
        //TODO nice exception
    );
    String graphqlFieldName = fetcherAnnotation.stringValue("field").orElseThrow(
        //TODO nice exception
    );

    MethodExecutionHandle<?, Object> handle = executionHandleLocator.createExecutionHandle(beanDefinition, (ExecutableMethod<Object, ?>) method);
    Object[] arguments = new Object[handle.getArguments().length];

    typeWirings.add(
        TypeRuntimeWiring.newTypeWiring(graphqlTypeName)
            .dataFetcher(graphqlFieldName, (DataFetchingEnvironment env) -> handle.invoke(arguments))
            .build()
    );
  }

  public List<TypeRuntimeWiring> getTypeWirings() {
    return Collections.unmodifiableList(typeWirings);
  }
}
