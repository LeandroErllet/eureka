package br.com.craftlife.eureka.loader.types;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Indica a classe de configuração do módulo, cada módulo deve ter apenas uma.
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Module {

    /**
     * Determina o nome do módulo
     */
    String value();

}
