package org.acme;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

@Dependent
public class ServicoInjetado {

  @PostConstruct
  void postConstruct() {
    System.out.println("Sou um serviço injetado que funciona perfeitamente com CDI!");
  }


  void chamar(String variavel) {
    System.out.println("Fui chamado com a variável: " + variavel);
  }
}