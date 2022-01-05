package org.acme;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class CasteloDeGraySkull {
  @Inject
  PortaoNormal portaoNormal;

  @Inject
  PortaoVip portaoVip;

  @Inject
  Porteiro porteiro;

  public void entrarNoCastelo(String nome) {
    System.out.println(nome + " está entrando no castelo");
    if(porteiro.identificarSeÉVip(nome)) {
      portaoVip.entrar(nome);
    } else {
      portaoNormal.entrar(nome);
    }
  }
}