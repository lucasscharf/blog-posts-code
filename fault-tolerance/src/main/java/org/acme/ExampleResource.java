package org.acme;

import java.time.LocalDateTime;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;

@Path("/")
public class ExampleResource {
  int contador = 0;

  @GET
  @Path("/timeout")
  public String helloComTimeout() throws Exception {
    try {
      return tentativaComSleep();
    } catch (TimeoutException e) {
      return "Tentativa com timeout tratado, seu serviço responderá rápido";
    }
  }

  @Timeout(value = 2000)
  String tentativaComSleep() throws Exception {
    Thread.sleep(5_000);
    return "hello com timeout";
  }

  @GET
  @Path("/retries")
  public String helloComRetry() throws Exception {
    tentativaComRetentativas();
    return "hello com retry";
  }

  @Retry(maxRetries = 5)
  void tentativaComRetentativas() {
    contador++;
    System.out.println("Tentativa número: " + contador + " às " + LocalDateTime.now());
    throw new RuntimeException();
  }

  @GET
  @Path("/fallback")
  public String helloComFallback() throws Exception {
    return tentativaComFallback();
  }

  @Fallback(fallbackMethod = "tratamentoComFallback")
  String tentativaComFallback() {
    throw new RuntimeException();
  }

  String tratamentoComFallback() {
    return "Hello com fallback tratado";
  }
  
}