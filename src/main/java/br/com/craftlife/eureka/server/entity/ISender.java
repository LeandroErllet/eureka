package br.com.craftlife.eureka.server.entity;

import br.com.craftlife.eureka.database.EurekaED;
import br.com.craftlife.eureka.resource.messages.Message;
import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.permission.PermissionAction;
import br.com.craftlife.eureka.server.permission.PermissionContext;

import java.util.Optional;

public interface ISender {

    boolean hasPermission(EurekaED ed, PermissionAction action);

    boolean hasPermission(Class<?> clazz, PermissionAction action);

    boolean hasPermission(Object object, PermissionAction action);

    boolean hasPermission(EurekaED ed, PermissionAction action, PermissionContext context);

    boolean hasPermission(Class<?> clazz, PermissionAction action, PermissionContext context);

    boolean hasPermission(Object object, PermissionAction action, PermissionContext context);

    ServerManager getServer();

    void sendMessage(Message message);

    void sendMessage(Optional<Message> message);

    String getName();

    boolean isPlayer();

}
