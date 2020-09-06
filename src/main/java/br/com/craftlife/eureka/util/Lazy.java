package br.com.craftlife.eureka.util;

import java.util.function.Consumer;
import java.util.function.Supplier;

public class Lazy<T> {

    private Supplier<T> supplier;

    private volatile T value;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
    }

    public static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<>(supplier);
    }

    public static <T> Lazy<T> of(T value) {
        Lazy lazy = new Lazy<>(null);
        lazy.value = value;
        return lazy;
    }

    public synchronized T get() {
        T val = value;
        if (val == null && supplier != null) {
            val = supplier.get();
            supplier = null;
            return val;
        }
        return val;
    }

    public void ifPresent(Consumer<T> consumer) {
        T val = value;
        if (val != null) {
            consumer.accept(val);
        }
    }

}
