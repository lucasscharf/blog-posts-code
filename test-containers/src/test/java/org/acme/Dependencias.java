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
