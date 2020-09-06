# Eureka - Banco de dados

## Migration

O Eureka utiliza do flyway para versionar as alterações do banco de dados.

Para criar uma alteração no banco de dados, crie uma pasta `sql` nos `resources` do seu módulo. Após isso crie um arquivo `.sql` com o seguinte [formato](https://flywaydb.org/documentation/migrations#naming):

`V1__Cria_tabela_warp.sql`

E com o conteúdo:

```sql
create table warp (
    nome varchar(16) not null,
    x float not null,
    y float not null,
    z float not null,
    mundo varchar(32) not null,
    primary key (nome)
);
```

Quando o módulo for inicializado, os arquivos serão executados conforme a versão especificada.

## EurekaED

Um ED (Entidade de Dados) define como um registro do banco de dados deve ser mapeado para um objeto Java. Segue um exemplo da representação da tabela anterior em um ED:

WarpED:

```java
@Getter // os EDs devem possuir getters e setter para todas as propriedades
@Setter
@Entity
@Table(name = "warp")               // <String> indica o tipo da chave primária
public class WarpED implements EurekaED<String> {

    @Id
    @Column(name = "nome")
    private String nome;

    @Column(name = "x")
    private Float x;

    @Column(name = "y")
    private Float y;

    @Column(name = "z")
    private Float z;

    @Column(name = "mundo")
    private String mundo;

    @Override
    public String getId() {
        return nome;
    }

    @Override
    public void setId(String id) {
        this.nome = id;
    }

}
```

## EurekaBD

Um BD (Banco de Dados) é uma classe que fornece métodos de CRUD (leitura e alteração de dados) prontos. Você também pode criar métodos no BD para consultas mais complexas.

Um exemplo de BD para o WarpED (WarpBD):

```java
                                 // <WarpED> indica o ED que esta BD gerencia
                                 //         <String> indica a chave primária da ED
public class WarpBD extends EurekaBD<WarpED, String> {

}
```

## Realizando operações

Dentro do seu módulo, basta injetar o seu BD para ter acesso a todas as funções:

```java
@Inject
private WarpBD warpBD;

public void criarWarp(String nome, Location location) {
    WarpED warp = new WarpED();
    warp.setNome(nome);
    warp.setX(location.getX());
    warp.setY(location.getY());
    warp.setZ(location.getZ());
    warp.setMundo(location.getWorld().getName());

    warpBD.inclui(warp); // insere o warp no banco de dados
}
```

## Criando consultas complexas

Para criar consultas complexas, você deve criar um método dentro do BD e obter um `CriteriaQuery` chamando o método `getCriteria()` do proprio BD. Para executar a consulta criada, existem os métodos `find` e `findAll` também dentro da classe.

Exemplo básico:

```java
public List<WarpED> buscaPorMundo(World world) {
    CriteriaBuilder builder = getCriteriaBuilder();
    CriteriaQuery<WarpED> criteria = getCriteria();
    Root<WarpED> root = criteria.from(WarpED.class);
    criteria
        .select(root)
        .where(builder.equal(root.get("mundo"), world.getName()));
    return findAll(criteria);
}
```

## Auditoria de dados

Quando se lida com informações sensíveis, pode ser necessário manter uma auditoria das alterações nos dados.

### Criando a ED com auditoria

Para realizar operações de auditoria você precisa utilizar uma ED especial de auditoria, a `EurekaCtrlED`, para isso, basta definir que sua classe estende ela:

```java
@Getter
@Setter
@Entity
@Table(name = "kit")
public class KitED extends EurekaCtrlED<String> {

    // Colunas da entidade...

}
```

### Definindo a estrutura da tabela

A ED com auditoria requer que sua tabela contenha 6 colunas extras, coloque-as na tabela seguindo o exemplo abaixo:

```sql
erk_created_by varchar(16) not null,
erk_created_by_ip varchar(15) not null,
erk_creation_date datetime not null,

erk_updated_by varchar(16) not null,
erk_updated_by_ip varchar(15) not null,
erk_update_date datetime not null
```

Cada coluna representa:

* *erk_created_by*: Nome do usuário que inseriu o recurso
* *erk_created_by_ip*: IP do usuário que inseriu o recurso
* *erk_creation_date*: Data e hora que o recurso foi criado
* *erk_created_by*: Nome do último usuário que atualizou o recurso
* *erk_created_by_ip*: IP do último usuário que atualizou o recurso
* *erk_creation_date*: Data e hora da última vez que o recurso foi atualizado

### Criando a BD com suporte a auditoria

Para criar a BD com suporte a auditoria basta estender a classe `EurekaCtrlBD` da mesma forma que é feita com as BD's comuns:

```java
public class KitBD extends EurekaCtrlBD<KitED, String> {

}
```

### Definindo os valores de auditoria

A ED de auditoria já define os valores corretos para cada coluna automaticamente, você só precisa chamar o método `update(...)` passando o `User` atual:

```java
@Inject
private KitBD kitBD;

public void test(User user) {
    KitED kit = new KitED();
    kit.setNome("teste");
    kit.update(user); // esse método atribuirá os valores corretamente para cada coluna de auditoria
    kitBD.inclui(user);
}
```

**Observação**: A ED de auditoria não permite valores nulos nas colunas, portanto certifique-se de sempre chamar o método `update` sempre que for manipular os dados.