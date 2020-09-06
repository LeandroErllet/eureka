# Eureka - Permissões

Organizar permissões é algo trabalhoso, por isso o eureka realiza uma normalização das permissões dos módulos. Com isso as permissões ficam prevísiveis e bem estruturadas.

## Estrutura das permissões

As permissões do eureka seguem um padrão Objeto-Ação, sendo:

* **Objeto**: Entidade, recurso ou função que o jogador está querendo interagir (acessar, alterar, criar, remover, etc.). Exemplos: warp, kit, chat, comando, etc.
* **Ação**: A ação que o jogador está querendo exercer sobre o **objeto** em questão.

A lista de ações é pré definida como:
* **Criar**: utilizada quando o player deseja criar um recurso, exemplo: criar kit
* **Atualizar**: utilizada quando o player deseja atualizar um recurso, exemplo: setar o spawn
* **Remover**: utilizada quando o player deseja remover um recurso, exemplo: deletar um warp
* **Listar**: utilizada quando o player deseja listar os recursos disponiveis, exemplo: listar admins online
* **Acessar**: utilizada quando o player deseja utilizar um recurso, exemplo: ativar um kit

Exemplos de Objeto-Ação:

| Comando        | Objeto | Ação    | Permissão efetiva  |
|----------------|--------|---------|--------------------|
| /delwarp spawn | warp   | remover | warp.remover.spawn |
| /kit pvp       | kit    | acessar | kit.acessar.pvp    |
| /warps         | warp   | listar  | warp.listar        |
| /createkit nb  | kit    | criar   | kit.criar          |

## Utilizando as permissões

Na classe `User` você tem três formas de realizar a verificação de permissões, são elas:

### Utilizando uma ED como *objeto*

Este modo é o correto ao se verificar a permissão de um player em relação a uma ED.

Para verificar, basta utilizar o método `hasPermission(EurekaED, PermissionAction)`, ex:

```java
public boolean test(User user, KitED kit) {
    return user.hasPermission(kit, PermissionAction.DELETE);
}
```

Utilizando esse método, a permissão será gerada com o padrão:

`<nome da ed>.<ação>.<id do objeto>`

**Observação¹:** O nome da ED é o mesmo nome da classe sem o sufixo ED e com as letras minúsculas.

**Observação²:** O ID do objeto não vale para a ação de **criar**.

### Utilizando uma classe como *objeto*

Este modo é o correto ao se verificar a permissão de um player em relação a um comando que não tem relação com banco de dados.

Para verificar, basta utilizar o método `hasPermission(Class, PermissionAction)`, ex:

```java
public boolean test(User user) {
    return user.hasPermission(VipChannel.class, PermissionAction.ACCESS);
}
```

Utilizando esse método, a permissão será gerada com o padrão:

`<nome da classe>.<ação>`

**Observação:** O nome da classe é em letras minúsculas.

### Utilizando uma instância como *objeto*

Este modo é o correto ao se verificar a permissão de um player em relação a objeto de tipo específico desconhecido.

Exemplo: Objeto do tipo Channel mas com subtipo VipChannel.

Para verificar, basta utilizar o método `hasPermission(Object, PermissionAction)`, ex:

```java
public boolean test(User user, Channel channel) {
    return user.hasPermission(channel, PermissionAction.ACCESS);
}
```

Utilizando esse método, a permissão será gerada com o padrão:

`<nome da classe>.<ação>`

**Observação:** O nome da classe é em letras minúsculas.

## Nomes customizados (não recomendado)

Em casos específicos, o nome da classe não é adequado para o nome do objeto. Para definir um nome, anote a classe com a anotação `@NamedPermission` para que a classe tenha outro nome durante a verificação de permissão. Exemplo:

```java
@NamedPermission("kit")
public class InternalKitImpl implements Kit {

}
```