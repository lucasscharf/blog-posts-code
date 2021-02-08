Me considero uma pessoa que pensa no bem comum e em ajudar o próximo. Por exemplo, caso eu estivesse eu encontrasse um gênio que me desse direito a um único desejo, eu escolheria que os pokémons existissem realmente no nosso mundo. Porém, se ele me desse direito a um segundo desejo, eu escolheria que todos os desenvolvedores soubessem usar os [test containers](https://www.testcontainers.org/).

#O que é?
O Testcontainers é uma biblioteca Java que permite subir instâncias dockers facilmente. Com essas instâncias, é possível ter acesso ao banco de dados, sistema de mensageria, cache, etc... 

Mas é importante ter atenção pra algo muito importante.
**É NECESSÁRIO TER O DOCKER INSTALADO NA MÁQUINA QUE FOR RODAR OS TESTES.**

#O que faremos?
O teste de hoje será um pouco mais elaborado. Vamos usar o que nós já aprendemos sobre [banco de dados](https://dev.to/lucasscharf/trabalhando-com-jpa-quarkus-panache-e-bolo-de-chocolate-36k7) e [mensageria](https://dev.to/lucasscharf/como-usar-kafka-num-projeto-quarkus-5d9c). Vamos criar uma classe que cria um Pokémon e armazena ele no c̶i̶n̶t̶o̶ ̶d̶e̶ ̶p̶o̶k̶e̶b̶ó̶l̶a̶s̶ banco de dados. 
Após isso, será disparado um evento ao kafka onde um treinador rival irá batalhar e nós recuperaremos o nosso pokémon para a batalha.

![Alt Text](https://dev-to-uploads.s3.amazonaws.com/i/crqs3wr9flns1x92i971.jpg)

#Hora da ação

Primeiramente vamos configurar nosso ambiente com as dependências necessárias. Por isso, é necessário adicionar a seguinte configuração no nosso pom.xml. Essas extensões configuram o conector de mensagens Kafka, a comunicação com banco de dados (via Panache) e o Test Containers. 

```xml
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-smallrye-reactive-messaging-kafka</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-jdbc-postgresql</artifactId>
    </dependency>
    <dependency>
      <groupId>io.quarkus</groupId>
      <artifactId>quarkus-hibernate-orm-panache</artifactId>
    </dependency>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>testcontainers</artifactId>
      <version>1.15.1</version>
      <scope>test</scope>
    </dependency>
    <dependency>
      <groupId>org.testcontainers</groupId>
      <artifactId>kafka</artifactId>
      <version>1.15.1</version>
      <scope>test</scope>
    </dependency>
```

Segundamente, vamos criar nossa classe de Pokémon. Ela é bem simples, tem o nome e o número dele. Isso é feito através da classe abaixo:

```java
package org.acme;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pokemon extends PanacheEntity{
  private int codigo;
  private String nome;

  public Pokemon() {
  }

  public Pokemon(int codigo, String nome) {
    this.codigo = codigo;
    this.nome = nome;
  }

  public int getCodigo() {
    return codigo;
  }

  public String getNome() {
    return nome;
  }

}

```

Agora vamos fazer nosso gerador de batalhas. Ao receber uma mensagem de que um treinador rival quer entrar em batalha (recebendo o nome do treinador), ele gravará no log o nome do treinador, pegará o primeiro pokémon disponível no banco de dados e gravará no log o nome desse pokémon. O código da batalha está baixo. 
```java
package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.reactive.messaging.annotations.Blocking;

@ApplicationScoped
public class GeradorDeBatalha {
  private static final int PRIMEIRO_ELEMENTO = 0;
  Logger logger = LoggerFactory.getLogger(getClass());

  @Incoming("novo-combate")
  @Blocking
  @Transactional
  public void iniciarCombate(String nomeDoTreinador) {
    logger.info("O treinador [{}] quer iniciar um combate", nomeDoTreinador);
    Pokemon pokemon = (Pokemon) Pokemon.listAll().get(PRIMEIRO_ELEMENTO);
    logger.info("[{}], eu escolho você!", pokemon.getNome());
  }
}
```

Vamos fazer o nosso teste. Por fins didáticos, não fazemos nenhum tipo de asserção sobre ele, apenas olhando 
o logo impresso. Como são threads diferentes que irão fazer o processamento (uma thread vai emitir a mensagem de batalha e a outra vai consumir), nós precisamos dar um tempo na nossa thread de teste para que a outra faça o seu trabalho.

O código para testes é esse aqui:

```java
package org.acme;

import javax.inject.Inject;
import javax.transaction.Transactional;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@QuarkusTestResource(Dependencias.class)
public class BatalhaTest {
  @Channel("iniciar-combate")
  @Inject
  Emitter<String> emissor;

  @BeforeEach
  @Transactional
  public void cadastrarPokemon() {
    Pokemon pokemon = new Pokemon(25, "Pikachu");
    pokemon.persist();
  }

  @Test
  public void combateSimples() throws Exception {
    emissor.send("Gary Carvalho");
    Thread.sleep(1_000);
  }
}
```

Feito isso, vamos configurar o **application.properties** com o banco de dados, um consumidor e um emissor de mensagens de combate (esse emissor será usado só para os testes).

```properties
quarkus.datasource.db-kind = postgresql
quarkus.datasource.username = ash
quarkus.datasource.password = pallet
quarkus.datasource.jdbc.url = jdbc:postgresql://localhost:5432/pokemon

quarkus.hibernate-orm.database.generation = update


mp.messaging.incoming.novo-combate.connector=smallrye-kafka
mp.messaging.incoming.novo-combate.topic=novo-combate
mp.messaging.incoming.novo-combate.value.deserializer=org.apache.kafka.common.serialization.StringDeserializer

%test.mp.messaging.outgoing.iniciar-combate.connector=smallrye-kafka
%test.mp.messaging.outgoing.iniciar-combate.topic=novo-combate
%test.mp.messaging.outgoing.iniciar-combate.value.serializer=org.apache.kafka.common.serialization.StringSerializer
```

Caso tenha ficado alguma dúvida sobre o funcionamento da parte de configuração, tem dois posts topzeiras [aqui](https://dev.to/lucasscharf/configurando-sua-aplicacao-java-com-quarkus-configproperty-2h2c) e [aqui](https://dev.to/lucasscharf/alterando-o-perfil-de-configuracao-do-quarkus-cn8).


Tudo certo né? Agora é só mandar o maven testar e podemos ser felizes. Certo? Errado!
![Alt Text](https://dev-to-uploads.s3.amazonaws.com/i/t81d1qm1t65inee243pw.png)

Como não existe nem o banco de dados rodando e nem o kafka, a aplicação nem subirá direito. 
![Alt Text](https://dev-to-uploads.s3.amazonaws.com/i/7wkhpofbnqzcqczwrxet.png)

Para que a aplicação suba corretamente, é necessário fazer as dependências rodarem e é aqui que os **tests containers** entram. 

Nós já configuramos eles, agora basta criarmos os containers para testes. Isso pode ser feito através da classe abaixo.

```java
package org.acme;

import java.util.HashMap;
import java.util.Map;

import org.testcontainers.containers.GenericContainer;
import org.testcontainers.containers.KafkaContainer;
import org.testcontainers.utility.DockerImageName;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

public class Dependencias implements QuarkusTestResourceLifecycleManager {
  private final KafkaContainer kafka = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka")); //0
  
  private static final DockerImageName POSTGRES_IMAGE = DockerImageName.parse("postgres:12-alpine"); // 1
  private GenericContainer<?> postgresContainer = new GenericContainer<>(POSTGRES_IMAGE); //2

  public Map<String, String> start() {
    postgresContainer.withEnv("POSTGRES_USER", "usuario")// 3
        .withEnv("POSTGRES_PASSWORD", "senha")
        .withExposedPorts(5432)// 4
        .withEnv("POSTGRES_DB", "bancoDeDados"); // 5
    postgresContainer.start();//6

    kafka.start();//7

    Map<String, String> properties = new HashMap<>();
    properties.put("quarkus.datasource.username", "usuario"); //8
    properties.put("quarkus.datasource.password", "senha");
    properties.put("quarkus.datasource.jdbc.url",//
        "jdbc:postgresql://"+ postgresContainer.getHost() +":" + postgresContainer.getFirstMappedPort() + "/bancoDeDados");//9
    properties.put("kafka.bootstrap.servers", kafka.getBootstrapServers());//10

    return properties;
  }

  public void stop() {
  }
}
```
Essa é a classe que cria os containers. Vamos entender o que ela faz?

* 0 - Container kafka que já tem tudo pré-configurado (ter um container desses é muito útil)
* 1 - Imagem docker para a utilização de um container genérico
* 2 - Container genérico, onde vamos configurar os valores para fazer nossos testes
* 3 - Configuração do usuário
* 4 - Configuração da senha
* 5 - Configuração do banco (notem que é uma boa prática usar valores diferentes dos que estão no **application.properties**)
* 6 - Expondo a porta interna 5432 (o testContainers poderá mapear essa porta interna para alguma outra porta externa)
* 7 - Iniciando o container postgres
* 8 - Iniciando o container kafka
* 9 - Atualizando as configurações que serão utilizadas durante o teste
* 10 - Na falta de um container específico, as configurações ficam assim e isso pode gerar erros
* 11 - Utilização de propriedade com container específico
* 12 - Retornando as propriedades

E nós só precisamos de mais um passo que é informar para a nossa classe de teste de que é para ele utilizar o nosso recurso de teste. Isso é feito colocando a anotação de classe
``@QuarkusTestResource(Dependencias.class)``. 

Agora sim é só rodar a nossa aplicação com o ``mvn test`` e correr pro abraço.

```shell
2021-02-08 12:08:14,720 INFO  [org.acm.GeradorDeBatalha_Subclass] (vert.x-worker-thread-1) O treinador [Gary Carvalho] quer iniciar um combate
2021-02-08 12:08:14,811 INFO  [org.acm.GeradorDeBatalha_Subclass] (vert.x-worker-thread-1) [Pikachu], eu escolho você!
```
#Considerações
Notei que boa parte dos meus textos são sobre testes e estou feliz com isso. Um mundo com mais testes, é um mundo mais feliz.

Ah, e o código de hoje pode ser encontrado no github.

