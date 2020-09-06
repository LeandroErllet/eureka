package br.com.craftlife.eureka.database.memory;

public interface IMemoryStorage {

    String get(String key);

    void remove(String key);

    void set(String key, String value, long expireSeconds);

    boolean containsKey(String key);

    void close();

    void connect();

}
