package br.com.craftlife.eureka.injector.listener;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import com.google.inject.spi.ProvisionListener;
import org.bukkit.event.Listener;

public class InjectionListener implements ProvisionListener {

    private final EurekaLoader loader;

    public InjectionListener(EurekaLoader loader) {
        this.loader = loader;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T value = provision.provision();
        if (value instanceof Listener) {
            loader.registerEvents((Listener) value);
        }
    }
}
