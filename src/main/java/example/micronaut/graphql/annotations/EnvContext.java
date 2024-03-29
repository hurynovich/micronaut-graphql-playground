package example.micronaut.graphql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

@Target(ElementType.PARAMETER)
public @interface EnvContext {
  String value() default "";
}
