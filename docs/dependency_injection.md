# Eureka - Injeção de dependências

Toda a framework do eureka é organizada para funcionar via injeção de dependência, mas o que é isso?

Sempre que necessitamos utilizar um BD em algum ponto do módulo devemos ter uma instância da classe, só que essa instância não pode ser criada utilizando `new` pois ela também tem outras dependências.

Para resolver esse problema de classes dependendo de outras existe o sistema de injeção de dependência. Para utilizá-lo basta anotar uma propriedade da classe com a anotação `@Inject` e você terá uma instância pronta pra usar, por exemplo:

```java
@Inject
private WarpBD warpBD;
```

Embora a propriedade esteja sem valor e não haja nenhum outro código atribuindo valor a ela, durante a execução do módulo um valor será atribuído dinamicamente.

Esse sistema não funciona apenas com BD's, você também pode injetar qualquer classe sua, nada mais de `getInstance()` :D

Exemplos de injeção possíveis:

* Classe principal do módulo
* `MessageProvider` para obter mensagens configuráveis
* `ServerManager` para métodos úteis do servidor
* Classe `DatabaseManager` (para configurações avançadas)
* Classe `EurekaLoader` (para configurações avançadas)

## Ciclo de vida

Todas as classes que você utilizar com injeção de dependência serão construídas no momento da injeção, ou seja, propriedades não serão compartilhadas entre as injeções.

Caso haja essa necessidade, você pode criar uma dependência `Singleton` (**não recomendado**), para fazer isso basta adicionar a anotação `@Singleton` na classe de dependência.