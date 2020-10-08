package ru.otus.generics;

/**
 * @author sergey
 * created on 23.11.18.
 */
public class GenericsMethod {

  public static void main(String[] args) {
    GenericsMethod genericsMethod = new GenericsMethod();
    genericsMethod.print(1, "value");
    genericsMethod.print(2, "value2");
  }

  private <K, V> void print(K key, V val) {
    System.out.println("key:" + key + ", val:" + val);
  }

}
