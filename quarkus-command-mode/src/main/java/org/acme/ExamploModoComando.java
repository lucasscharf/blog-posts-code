package org.acme;

import javax.inject.Inject;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class ExamploModoComando implements QuarkusApplication {

  @Inject
  ServicoInjetado servico;

  @Override
  public int run(String... args) throws Exception {

    if (args.length > 0) {
      servico.chamar(args[0]);
    } else {
      servico.chamar("");
    }

    return 0;
  }
}