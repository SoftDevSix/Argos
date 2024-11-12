package com.softdevsix.argos.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

/** Repository */
@Entity
public class Project {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  private Integer id;

  public Integer getId() {
    return id;
  }

  public void setId(int id){
    this.id = id;
  }
}
