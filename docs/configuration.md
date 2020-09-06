# Eureka - Configuração

Gerenciar arquivos de configuração deve ser simples e objetivo, para fazer isso o eureka possui um sistema de injeção de configurações dentro do seu módulo.

Para obter uma configuração em qualquer lugar do seu módulo, basta anotar uma propriedade com `@Config` e o valor será [injetado](dependency_injection.md) durante a execução, como por exemplo:

```java
@Config("kit.delay")
private ConfigInstance<Integer> delay;
```

A classe `ConfigInstance` também possui métodos utilitários para alterar, salvar e recarregar a configuração.

**Observação**: O método `getValue()`, utilizado para obter o valor da configuração, retorna um `Optional` para que valores nulos sejam devidamente tratados. Certifique-se de que haja uma alternativa para esses valores utilizando `isPresent()`, `orElse(...)` ou algo do tipo.

## Configuração de outros arquivos `.yml`

Para definir outro arquivo `.yml` como fonte da configuração basta adicionar outra propriedade na anotação:

```java
@Config(value = "mensagens.sucesso", resource = "kit.yml")
private ConfigInstance<String> mensagem;
```

## Obtendo o valor puro

A injeção das configurações é realizada utilizando um `ConfigInstance` para que seja possível recarregar os valores durante a execução. Caso isso não seja necessário, também é possivel receber o valor da configuração diretamente:

```java
@Config("mensagens.sucesso")
private String mensagem;
```

## Obtendo a configuração completa

Para manipular a configuração completa, veja a [documentação sobre recursos](resources.md).