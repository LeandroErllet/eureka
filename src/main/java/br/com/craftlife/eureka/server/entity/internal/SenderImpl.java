package br.com.craftlife.eureka.server.entity.internal;

import br.com.craftlife.eureka.database.EurekaED;
import br.com.craftlife.eureka.resource.messages.Message;
import br.com.craftlife.eureka.server.ServerManager;
import br.com.craftlife.eureka.server.entity.ISender;
import br.com.craftlife.eureka.server.permission.PermissionAction;
import br.com.craftlife.eureka.server.permission.PermissionContext;
import br.com.craftlife.eureka.util.ClassUtils;
import org.bukkit.command.CommandSender;

import java.util.Optional;

public abstract class SenderImpl implements ISender {

    private final CommandSender sender;

    private final ServerManager serverManager;

    protected SenderImpl(CommandSender sender, ServerManager serverManager) {
        this.sender = sender;
        this.serverManager = serverManager;
    }

    @Override
    public void sendMessage(Message message) {
        sender.sendMessage(message.getSource());
    }

    @Override
    public void sendMessage(Optional<Message> message) {
        message.ifPresent(msg -> sender.sendMessage(msg.getSource()));
    }

    @Override
    public ServerManager getServer() {
        return serverManager;
    }

    @Override
    public boolean hasPermission(EurekaED ed, PermissionAction action) {
        return hasPermission(ed, action, PermissionContext.SELF);
    }

    @Override
    public boolean hasPermission(Class<?> clazz, PermissionAction action) {
        return hasPermission(clazz, action, PermissionContext.SELF);
    }

    @Override
    public boolean hasPermission(Object object, PermissionAction action) {
        return hasPermission(object, action, PermissionContext.SELF);
    }

    @Override
    public boolean hasPermission(EurekaED ed, PermissionAction action, PermissionContext context) {
        if (ed == null || action == null || context == null) {
            return false;
        }
        StringBuilder permission = new StringBuilder(ClassUtils.getPermissionFromClass(ed.getClass()));
        if (context != PermissionContext.SELF) {
            permission.append(".");
            permission.append(context.getDescription());
        }
        permission.append(".");
        permission.append(action.getDescription());
        Object id = ed.getId();
        if (id != null && action != PermissionAction.CREATE) {
            permission.append(".");
            permission.append(String.valueOf(id));
        }
        return sender.hasPermission(permission.toString());
    }

    @Override
    public boolean hasPermission(Class<?> clazz, PermissionAction action, PermissionContext context) {
        if (clazz == null || action == null || context == null) {
            return false;
        }
        StringBuilder permission = new StringBuilder(ClassUtils.getPermissionFromClass(clazz));
        if (context != PermissionContext.SELF) {
            permission.append(".");
            permission.append(context.getDescription());
        }
        permission.append(".");
        permission.append(action.getDescription());
        return sender.hasPermission(permission.toString());
    }

    @Override
    public boolean hasPermission(Object object, PermissionAction action, PermissionContext context) {
        if (object == null) {
            return false;
        }
        return hasPermission(object.getClass(), action, context);
    }

    @Override
    public String getName() {
        return sender.getName();
    }
}
