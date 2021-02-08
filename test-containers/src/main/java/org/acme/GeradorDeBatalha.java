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
