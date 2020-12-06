package org.acme;

import javax.inject.Inject;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConsumidorDeTexto {
  Logger logger = LoggerFactory.getLogger(getClass());

  @Incoming("consumidor2")
  public void consumir2(String texto) throws Exception {
    logger.info("Texto2: " + texto);
  }
}
