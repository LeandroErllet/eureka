package br.com.craftlife.eureka.resource.messages;

import br.com.craftlife.eureka.resource.FileResource;

import java.util.Optional;

public class MessageProvider {

    private final FileResource resource;

    public MessageProvider(FileResource resource) {
        this.resource = resource;
    }

    public Optional<Message> getMessage(String path) {
        if (resource == null) {
            return Optional.empty();
        }
        return resource.get(path, String.class)
                .flatMap(msg -> Optional.of(new Message(msg)));
    }

    public Optional<Message> getColored(String path) {
        if (resource == null) {
            return Optional.empty();
        }
        return resource.get(path, String.class)
                .flatMap(msg -> Optional.of(new Message(msg).colored()));
    }

    public boolean reload() {
        return resource.reload();
    }

}
