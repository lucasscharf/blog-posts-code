package org.acme;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Pokemon extends PanacheEntity{
  private int codigo;
  private String nome;

  public Pokemon() {
  }

  public Pokemon(int codigo, String nome) {
    this.codigo = codigo;
    this.nome = nome;
  }

  public int getCodigo() {
    return codigo;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public String toString() {
    StringBuilder builder = new StringBuilder();
    builder.append("Pokemon [codigo=");
    builder.append(codigo);
    builder.append(", nome=");
    builder.append(nome);
    builder.append("]");
    return builder.toString();
  }

}
