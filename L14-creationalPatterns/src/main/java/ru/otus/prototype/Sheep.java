package ru.otus.prototype;

import java.util.Objects;

class Sheep implements CloneableObject<Sheep> {
  private String name;

  Sheep(String name) {
    this.name = name;
  }

  public String getName() {
    return name;
  }

  void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "Sheep{" +
        "name='" + name + '\'' +
        '}';
  }

  @Override
  public Sheep copy() {
    return new Sheep(name);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Sheep sheep = (Sheep) o;
    return Objects.equals(name, sheep.name);
  }

  @Override
  public int hashCode() {

    return Objects.hash(name);
  }
}
