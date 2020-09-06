# Eureka - Injeção de recursos

Em determinados momentos receber apenas um valor específico da configuração não é suficiente, para resolver esse problema existe a injeção de recursos.

Os recursos são arquivos `.yml` de configuração que seu módulo pode precisar, isso também inclui o `config.yml`.

## Injetando um recurso

Para [injetar](dependency_injection.md) um recurso basta utilizar a anotação `@InjectResource` em uma propriedade do tipo `FileResource`, como por exemplo:

```java
@InjectResource("config.yml")
private FileResource config;
```

A classe `FileResource` possui métodos para gerenciar o recurso além de obter valores.

**Observação**: Os métodos de obter configurações retornam um `Optional` para que valores nulos sejam devidamente tratados. Certifique-se de que haja uma alternativa para esses valores utilizando `isPresent()`, `orElse(...)` ou algo do tipo.

## Carregando um recurso dinamicamente

Em casos específicos é necessário que o recurso seja carregado no código, para estes casos você pode injetar o `EurekaLoader` e obter o provedor de recursos com `getResourceProvider()`, por exemplo:

```java
@Inject
private EurekaLoader loader;

public void test() throws IOException {
    ResourceProvider provider = loader.getResourceProvider();
    FileResource resource = provider.loadResource("teste.yml");
    // manipula o recurso
}
```

Este modo de carregamento não é recomendado, ao utilizar tenha certeza de que não há outra forma de obter o recurso.