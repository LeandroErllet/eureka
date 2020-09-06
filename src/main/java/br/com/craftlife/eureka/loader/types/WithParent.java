package br.com.craftlife.eureka.loader.types;

import br.com.craftlife.eureka.module.EurekaModule;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface WithParent {

    Class<? extends EurekaModule> value();

}
