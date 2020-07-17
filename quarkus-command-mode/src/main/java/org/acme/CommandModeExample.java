package org.acme;

import javax.inject.Inject;

import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class CommandModeExample implements QuarkusApplication {

  @Inject
  InjectedService service;

  @Override
  public int run(String... args) throws Exception {

    if (args.length > 0) {
      service.call(args[0]);
    } else {
      service.call("");
    }

    return 0;
  }
}