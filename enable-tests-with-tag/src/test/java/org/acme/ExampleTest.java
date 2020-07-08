package org.acme;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Tag;

@QuarkusTest
public class ExampleTest {
  @Test
  public void testWithoutTag() {
    System.out.println("Running test without tag...");
  }

  @Test
  @Tag("another")
  public void testWithAnotherTag() {
    System.out.println("Running test with another...");
  }

  @Tag("integration")
  @Test
  public void testWithIntegrationTag() {
    System.out.println("Running integration test...");
  }
}