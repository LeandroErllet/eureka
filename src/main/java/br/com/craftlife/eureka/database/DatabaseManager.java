package br.com.craftlife.eureka.database;

import br.com.craftlife.eureka.database.memory.GenericStorage;
import br.com.craftlife.eureka.database.memory.IMemoryStorage;
import br.com.craftlife.eureka.loader.types.EurekaLoader;
import br.com.craftlife.eureka.util.Lazy;
import lombok.Getter;
import lombok.Setter;
import org.flywaydb.core.Flyway;
import org.hibernate.SessionFactory;
import org.hibernate.boot.Metadata;
import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistry;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Environment;

import javax.persistence.EntityManager;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

public class DatabaseManager {

    @Getter
    @Setter
    private DatabaseConfiguration databaseConfiguration;

    @Getter
    private IMemoryStorage memoryStorage = new GenericStorage(Lazy.of(() -> databaseConfiguration));

    private StandardServiceRegistry serviceRegistry;

    private SessionFactory sessionFactory;

    private final List<Class<?>> registeredClasses = new ArrayList<>();

    private final Map<Long, EntityManager> threadEntityManager = new ConcurrentHashMap<>();

    public void migrate(EurekaLoader loader) {
        disableLogs();
        String historyTable = "schema_history_" + loader.getModuleName().toLowerCase();

        Flyway flyway = new Flyway(loader.getModule().getClass().getClassLoader());

        flyway.setLocations("/sql");
        flyway.setTable(historyTable);
        flyway.setDataSource(databaseConfiguration.getUrl(), databaseConfiguration.getUser(), databaseConfiguration.getPassword());

        flyway.setBaselineOnMigrate(true);
        flyway.migrate();
    }

    public void enable() {
        try {
            memoryStorage.connect();
            disableLogs();
            StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

            Map<String, String> settings = new HashMap<>();
            settings.put(Environment.DRIVER, databaseConfiguration.getDriver());
            settings.put(Environment.URL, databaseConfiguration.getUrl());
            if (databaseConfiguration.getUser() != null) {
                settings.put(Environment.USER, databaseConfiguration.getUser());
            }
            if (databaseConfiguration.getPassword() != null) {
                settings.put(Environment.PASS, databaseConfiguration.getPassword());
            }
            settings.put(Environment.DIALECT, databaseConfiguration.getDialect());
            settings.put(Environment.C3P0_MAX_SIZE, "2");
            settings.put(Environment.C3P0_MIN_SIZE, "2");
            settings.put(Environment.C3P0_TIMEOUT, "5000");
            settings.put(Environment.C3P0_MAX_STATEMENTS, "0");

            registryBuilder.applySettings(settings);

            serviceRegistry = registryBuilder.build();

            MetadataSources sources = new MetadataSources(serviceRegistry);
            registeredClasses.forEach(sources::addAnnotatedClass);

            Metadata metadata = sources.getMetadataBuilder().build();

            sessionFactory = metadata.getSessionFactoryBuilder().build();

        } catch (Exception e) {
            e.printStackTrace();
            unload();
        }
    }

    public void unload() {
        if (serviceRegistry != null) {
            StandardServiceRegistryBuilder.destroy(serviceRegistry);
            serviceRegistry = null;
            threadEntityManager.values().forEach(EntityManager::close);
            threadEntityManager.clear();
        }
    }

    public EntityManager getEntityManager() {
        return threadEntityManager.computeIfAbsent(Thread.currentThread().getId(), (t) -> sessionFactory.createEntityManager());
    }

    private void disableLogs() {
        Logger.getLogger("org.flywaydb").setLevel(Level.WARNING);
        Logger.getLogger("org.hibernate").setLevel(Level.WARNING);
        System.setProperty("com.mchange.v2.log.FallbackMLog.DEFAULT_CUTOFF_LEVEL", "WARNING");
        System.setProperty("com.mchange.v2.log.MLog", "com.mchange.v2.log.FallbackMLog");
    }

    public void registerClass(Class<?> clazz) {
        registeredClasses.add(clazz);
    }

}
