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
  public void init() {
    Pokemon pokemon = new Pokemon(25, "Pikachu");
    pokemon.persist();
  }

  @Test
  public void combateSimples() throws Exception {
    emissor.send("Gary Carvalho");
    Thread.sleep(1_000);
  }
}
