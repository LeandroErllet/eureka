package br.com.craftlife.eureka.server.permission;

import lombok.Getter;

public enum PermissionAction {

    CREATE("criar"), UPDATE("atualizar"), DELETE("remover"),
    LIST("listar"), ACCESS("acessar");

    @Getter
    private final String description;

    PermissionAction(String description) {
        this.description = description;
    }

}
