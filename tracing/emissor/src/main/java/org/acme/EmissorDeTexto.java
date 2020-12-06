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

@Path("/emissor")
public class EmissorDeTexto {

  @Channel("emissor")
  @Inject
  Emitter<String> emitter;

  Logger logger = LoggerFactory.getLogger(getClass());

  @POST
  @Path("/{texto}")
  public void hello(@PathParam("texto") String texto) {
    logger.info("Emitindo mensagens...");
    for (int i = 0; i < 10; i++) {
      emitter.send(texto);
    }
    logger.info("Mensagens emitidas!");
  }

}