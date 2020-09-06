# Eureka

Este projeto contém uma framework para a criação de plugins Spigot estáveis e bem arquitetados.

Utilidades:

* Sistema de injeção de dependência
* Injeção de propriedades de configurações (.yml)
* Facilitador de configuração de mensagens
* Declaração de comandos facilitada (utilizando [ACF](https://github.com/aikar/commands))
* Camada pré definida pra acesso ao banco de dados (hibernate)
* Sistema de auditoria simplificada
* Sistema de permissionamento padronizado
* API simplificada para manipulação de entidades do jogo

## Documentação extra

Após criar seu primeiro módulo, aprenda mais sobre a framework:

* [Injeção de dependência](docs/dependency_injection.md)
* [Banco de dados](docs/database.md)
* [Configuração](docs/configuration.md)
* [Injeção de recursos](docs/resources.md)
* [Permissões](docs/permissions.md)

## Módulos eureka

A framework possui dois tipos de módulos, módulos pais e módulos filhos, os módulos filhos herdam conexão de banco de dados e dependências dos módulos pais.

### Criando um módulo

Primeiro, certifique-se de que a dependência esteja no seu `pom.xml`:

```xml
<dependency>
    <groupId>br.com.craftlife</groupId>
    <artifactId>eureka</artifactId>
    <version>3.0.0</version>
</dependency>
```

Para criar um módulo, basta definir na classe principal que ele herda a classe `EurekaModule`:

```java
public class TestModule extends EurekaModule {

    @Override
    public void init() {
        // Comando executado quando o módulo for devidamente ativado
    }

}
```

### Configurando banco de dados

Existem duas formas de configurar o acesso ao banco de dados: a automática e manual.

#### Configurando automaticamente

Para configurar automaticamente, é necessário que exista um `config.yml` ou `database.yml` com a seguinte estrutura:

```yaml
database:
  url: 'jdbc:mysql://127.0.0.1/teste' # URL de conexão do banco de dados
  user: 'test' # Usuário do banco
  password: '12345' # Senha do usuário
  dialect: 'org.hibernate.dialect.MySQLDialect' # Dialeto do hibernate (opcional, apenas necessário caso seja utilizado outro banco de dados)
  driver: 'com.mysql.jdbc.Driver' # Driver de acesso (opcional, apenas necessário caso seja utilizado outro banco de dados)

```

Dentro da classe principal do módulo, você deve sobrescrever o método `configure()` com o seguinte conteúdo:

```java
@Override
public void configure() {
    easySetup();
}
```

Dentro da classe principal do módulo, você deve sobrescrever o método `init()` com o seguinte conteúdo:

```java
@Override
public void init() {
    easySetup();
}
```

Após isso o eureka buscará as configurações e realizará o restante automaticamente.

#### Configurando manualmente

Para configurar manualmente, você deve injetar o `DatabaseManager` e sobrescrever o método `configure()` para definir as configurações, por exemplo:

```java
@Inject
private DatabaseManager manager;

@Override
public void configure() {
    DatabaseConfiguration configuration = new DatabaseConfiguration();
    configuration.setUrl("jdbc:mysql://127.0.0.1/teste");
    configuration.setUser("test");
    configuration.setPassword("12345");
    manager.setDatabaseConfiguration(configuration);
}
```

### Criando comandos

Para criar um comando, basta criar um método similar a este exemplo:

```java
@CommandAlias("res|residence|resadmin")
public class ResidenceCommand extends BaseCommand {

    @Default
    @Subcommand("list")
    @Syntax("<+tag> [page] or [page]")
    @CommandCompletion("@residencelist")
    @Description("Lists all of your or another players residences.")
    public static void onList(Player player, String[] args) {
        if (args.length == 0) {
            EmpireUser user = EmpireUser.getUser(player);

            Util.sendMsg(player, "&bYou currently have &a" + user.numRes +
                "&b/&a" + user.maxRes + "&b Residences.");

            Residences.listGlobalResidences(player);
        } else {
            if (args[0].startsWith("+")) {
                int page = (args.length == 2 && NumUtil.isInteger(args[1])) ? Integer.parseInt(args[1]) : 1;
                TagManager.listTaggedResidences(player, args[0].substring(1), page);
            } else {
                Residences.listGlobalResidences(player, args[0]);
            }
        }
    }
}
```

Mais exemplos estão disponíveis no repositório do [ACF](https://github.com/aikar/commands).
