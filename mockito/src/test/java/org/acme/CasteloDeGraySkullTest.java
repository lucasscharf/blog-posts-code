package org.acme;

import javax.inject.Inject;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;

@QuarkusTest
public class CasteloDeGraySkullTest {
  @Inject
  CasteloDeGraySkull castelo;

  @InjectMock
  Porteiro porteiroMock;

  @Test
  public void testeEntrarNoNormal() {
    castelo.entrarNoCastelo("He-man");
  }

  @Test
  public void testeEntrarNoVip() {
    Mockito.when(porteiroMock.identificarSeÉVip(Mockito.anyString())).thenReturn(true);
    castelo.entrarNoCastelo("She-ha");
  }
}
