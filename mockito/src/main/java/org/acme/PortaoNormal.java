package org.acme;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PortaoNormal {
  public void entrar(String nome) {
    System.out.println(nome + " entrou pelo portão normal");
  }
}