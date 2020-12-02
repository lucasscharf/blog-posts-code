package com;

import javax.persistence.Entity;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

@Entity
public class Bolo extends PanacheEntity {
  public String nome;
  public String descricao;

  public Bolo() {
  }
  
  public Bolo(String nome, String descricao) {
    this.nome = nome;
    this.descricao = descricao;
  }

}