package br.com.craftlife.eureka.resource;

import java.util.Optional;
import java.util.Set;

public interface ResourceSection {

    Optional<Object> get(String path);

    <T> Optional<T> get(String path, Class<T> type);

    Optional<String> getString(String path);

    Optional<Double> getDouble(String path);

    Optional<Integer> getInt(String path);

    void set(String path, Object value);

    ResourceSection getSection(String path);

    Set<String> getKeys();

}
