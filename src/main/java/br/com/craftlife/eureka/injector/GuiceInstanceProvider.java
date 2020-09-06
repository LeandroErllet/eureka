package br.com.craftlife.eureka.injector;

import com.google.inject.Injector;

import java.util.function.Function;

public class GuiceInstanceProvider implements Function<Class, Object> {

    private final Injector injector;

    public GuiceInstanceProvider(Injector injector) {
        this.injector = injector;
    }

    @Override
    public Object apply(Class clazz) {
        return injector.getInstance(clazz);
    }
}
