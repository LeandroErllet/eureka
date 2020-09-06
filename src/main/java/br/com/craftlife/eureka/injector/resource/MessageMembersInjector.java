package br.com.craftlife.eureka.injector.resource;

import br.com.craftlife.eureka.resource.messages.Message;
import br.com.craftlife.eureka.resource.messages.MessageProvider;
import com.google.inject.MembersInjector;

import java.lang.reflect.Field;

class MessageMembersInjector<T> implements MembersInjector<T> {

    private final Field field;

    private final MessageProvider messageProvider;

    private Message message;

    private final InjectMessage injectMessage;

    public MessageMembersInjector(Field field, MessageProvider messageProvider) {
        this.field = field;
        this.messageProvider = messageProvider;
        this.injectMessage = field.getAnnotation(InjectMessage.class);
        initResource();
    }

    private void initResource() {
        field.setAccessible(true);
        message = messageProvider.getMessage(injectMessage.value()).orElse(null);
    }

    @Override
    public void injectMembers(T instance) {
        try {
            field.set(instance, message);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
