package br.com.craftlife.eureka.database.memory;

import br.com.craftlife.eureka.database.DatabaseConfiguration;
import io.lettuce.core.RedisClient;
import io.lettuce.core.SetArgs;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;

class RedisStorage implements IMemoryStorage {

    private final DatabaseConfiguration configuration;

    private StatefulRedisConnection<String, String> connection;

    private RedisCommands<String, String> commands;

    private RedisClient client;

    RedisStorage(DatabaseConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void connect() {
        client = RedisClient.create(configuration.getRedisUrl());
        connection = client.connect();
        commands = connection.sync();
    }

    @Override
    public String get(String key) {
        return commands.get(key);
    }

    @Override
    public void remove(String key) {
        commands.del(key);
    }

    @Override
    public void set(String key, String value, long expireSeconds) {
        commands.set(key, value, new SetArgs().ex(expireSeconds));
    }

    @Override
    public boolean containsKey(String key) {
        return commands.exists(key) == 1;
    }

    @Override
    public void close() {
        commands = null;
        connection.close();
        client.shutdown();
    }

}
