package br.com.craftlife.eureka.resource.messages;

import lombok.Getter;
import org.bukkit.ChatColor;

import java.util.regex.Pattern;

public final class Message {

    private static final Pattern VALID_PARAM = Pattern.compile("^[a-zA-Z0-9_]+$");

    @Getter
    private final String source;

    Message(String source) {
        this.source = source;
    }

    public Message colored() {
        return new Message(ChatColor.translateAlternateColorCodes('&', source));
    }

    public Message set(String param, String value) {
        if (!VALID_PARAM.matcher(param).matches()) {
            return this;
        }
        return new Message(source.replace("{" + param + "}", value));
    }

}
