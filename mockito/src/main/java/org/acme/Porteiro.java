package org.acme;
import javax.enterprise.context.ApplicationScoped;


@ApplicationScoped
public class Porteiro {
  public boolean identificarSeÉVip(String nome) {
    throw new RuntimeException("O porteiro faltou!");
  }
}