package br.com.craftlife.eureka.injector.command;

import br.com.craftlife.eureka.loader.types.EurekaLoader;
import co.aikar.commands.BaseCommand;
import com.google.inject.spi.ProvisionListener;

public class InjectionCommand implements ProvisionListener {

    private final EurekaLoader loader;

    public InjectionCommand(EurekaLoader loader) {
        this.loader = loader;
    }

    @Override
    public <T> void onProvision(ProvisionInvocation<T> provision) {
        T value = provision.provision();
        if (value instanceof BaseCommand) {
            loader.registerCommands((BaseCommand) value);
        }
    }
}
