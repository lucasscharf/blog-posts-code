package com;

import javax.enterprise.context.ApplicationScoped;

import org.eclipse.microprofile.reactive.messaging.Incoming;

@ApplicationScoped
public class BoloKafkaConsumer {
  
  @Incoming("add-bolo-consumer")
  public void consumeAdd(String bolo) {
    System.out.println("Bolo adicionado: " + bolo);
  }

  @Incoming("delete-bolo-consumer")
  public void consumeDelete(String nome) {
    System.out.println("Nome do bolo deletado: " + nome);
  }
}
