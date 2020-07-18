package org.acme.schedule;

import javax.enterprise.context.ApplicationScoped;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import java.util.concurrent.TimeUnit;

@ApplicationScoped  // (1)
public class SocTumPowGenerator {

  private static final Logger log 
      = LoggerFactory.getLogger(SocTumPowGenerator.class); // (2)

  @Scheduled(every = "11s") // (3)
  void soc() {
    log.info("soc");
  }

  @Scheduled(cron = "0/17 * * * * ?") // (4)
  void tum() {
    log.info("tum");
  }

  @Scheduled(cron = "{cron.expr}") // (5)
  void pow() {
    log.info("pow");
  }

  @Scheduled(every = "{every.expr}") // (6)
  void kabam() {
    log.info("kabam");
  }

  @Scheduled(delay = 1,  // (7)
            delayUnit = TimeUnit.MINUTES, // (8) 
            every = "3s") // (9)
  void kataplow() {
    log.info("kataplow");
  }
}