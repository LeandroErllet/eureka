package br.com.craftlife.eureka.server.permission;

import lombok.Getter;

public enum PermissionContext {

    SELF("self"), OTHER("others");

    @Getter
    private final String description;

    PermissionContext(String description) {
        this.description = description;
    }

}
