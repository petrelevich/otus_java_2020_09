package ru.otus.generics.bounds.entries;

/**
 * @author sergey
 * created on 23.11.18.
 */
public class WildCat extends Cat {
  private String name;

  public WildCat(String name) {
    this.name = name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Override
  public String toString() {
    return "WildCat, name:" + name;
  }
}
