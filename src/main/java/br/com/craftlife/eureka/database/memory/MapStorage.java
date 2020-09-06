package br.com.craftlife.eureka.database.memory;

import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.logging.Level;

class MapStorage implements IMemoryStorage {

    private final HashMap<String, MapValue> map = new HashMap<>();

    MapStorage() {
        Bukkit.getLogger().log(Level.WARNING, "Conexao redis nao encontrada, usando HashMap como alternativa");
    }

    @Override
    public String get(String key) {
        MapValue value = map.get(key);
        if (!value.isExpired()) {
            return value.getValue();
        }
        remove(key);
        return null;
    }

    @Override
    public void remove(String key) {
        map.remove(key);
    }

    @Override
    public void set(String key, String value, long expireSeconds) {
        map.put(key, new MapValue(value, expireSeconds));
    }

    @Override
    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    @Override
    public void close() {
        map.clear();
    }

    @Override
    public void connect() {
        map.clear();
    }
}
