package org.acme;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.eclipse.microprofile.opentracing.Traced;
import org.eclipse.microprofile.reactive.messaging.Incoming;

public class ConsumidorDeTexto {
    Logger logger = LoggerFactory.getLogger(getClass());

  @Channel("emissor2")
  @Inject
  Emitter<String> emitter;

  @Incoming("consumidor")
  public void consumir(String texto) throws Exception {
    Thread.sleep(2000);
    logger.info("Texto: " + texto);
    emitter.send(texto);
  }

  @Incoming("consumidor2")
  public void consumir2(String texto) throws Exception {
    logger.info("Texto2: " + texto);
  }
}
