package org.acme;

import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.smallrye.common.annotation.Blocking;

public class ConsumidorDeTexto {
  Logger logger = LoggerFactory.getLogger(getClass());

  // @Channel("emissor2")
  // @Inject
  // Emitter<String> emitter;

  @Incoming("consumidor")
  @Outgoing("emissor2")
  @Blocking
  public String consumir(String texto) throws Exception {
    Thread.sleep(2000);
    logger.info("Texto: " + texto);
    // emitter.send(texto);
    return texto;
  }

  @Incoming("consumidor2")
  public void consumir2(String texto) throws Exception {
    logger.info("Texto2: " + texto);
  }
}
