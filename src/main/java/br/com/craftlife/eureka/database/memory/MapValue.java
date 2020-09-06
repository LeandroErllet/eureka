package br.com.craftlife.eureka.database.memory;

import lombok.Getter;

class MapValue {

    @Getter
    private final String value;

    private final long created;

    private final long expiration;

    MapValue(String value, long expiration) {
        this.value = value;
        this.created = System.currentTimeMillis() / 1000;
        this.expiration = expiration;
    }

    public boolean isExpired() {
        long now = System.currentTimeMillis() / 1000;
        long diff = now - created;
        return diff >= expiration;
    }

}
