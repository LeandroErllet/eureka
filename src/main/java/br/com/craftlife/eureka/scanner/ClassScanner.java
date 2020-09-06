package br.com.craftlife.eureka.scanner;

import br.com.craftlife.eureka.database.DatabaseManager;
import lombok.Getter;
import org.reflections.Reflections;
import org.reflections.scanners.TypeAnnotationsScanner;

import javax.persistence.Converter;
import javax.persistence.Embeddable;
import javax.persistence.Entity;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ClassScanner {

    @Getter
    private final Class base;
    private final Set<Class> entities = new HashSet<>();
    private final ClassLoader loader;

    public ClassScanner(Class base, ClassLoader loader) {
        this.base = base;
        this.loader = loader;
    }

    public void scan() {
        String pack = base.getPackage().getName();
        String defaultPackage = DatabaseManager.class.getPackage().getName();
        Reflections reflections = new Reflections(pack, defaultPackage, loader, new TypeAnnotationsScanner());
        entities.addAll(reflections.getTypesAnnotatedWith(Entity.class, true));
        entities.addAll(reflections.getTypesAnnotatedWith(Embeddable.class, true));
        entities.addAll(reflections.getTypesAnnotatedWith(Converter.class, true));
    }

    public Set<Class> getEntities() {
        return Collections.unmodifiableSet(entities);
    }
}
