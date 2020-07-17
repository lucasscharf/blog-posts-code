package org.acme;

import javax.annotation.PostConstruct;
import javax.enterprise.context.Dependent;

@Dependent
public class InjectedService {

  @PostConstruct
  void postConstruct() {
    System.out.println("I'm an injected service and work well with CDI");
  }


  void call(String variable) {
    System.out.println("I'm called with variable: " + variable);
  }
}