package example.micronaut.graphql.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

//TODO: javadoc
@Target(ElementType.PARAMETER)
public @interface Attribute {
  //TODO: javadoc
  String value() default "";
}

