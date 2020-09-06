package br.com.craftlife.eureka.database.memory;

import br.com.craftlife.eureka.database.DatabaseConfiguration;
import br.com.craftlife.eureka.util.Lazy;
import lombok.experimental.Delegate;

public class GenericStorage implements IMemoryStorage {

    private interface Ignore {
        void connect();
    }

    @Delegate(excludes = Ignore.class)
    private IMemoryStorage selected;

    private final Lazy<DatabaseConfiguration> lazyConfiguration;

    public GenericStorage(Lazy<DatabaseConfiguration> lazyConfiguration) {
        this.lazyConfiguration = lazyConfiguration;
    }

    @Override
    public void connect() {
        DatabaseConfiguration configuration = lazyConfiguration.get();
        if (configuration.getRedisUrl() != null) {
            selected = new RedisStorage(configuration);
        } else {
            selected = new MapStorage();
        }
        selected.connect();
    }

}
