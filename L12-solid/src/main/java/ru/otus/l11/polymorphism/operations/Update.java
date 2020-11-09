package ru.otus.l11.polymorphism.operations;

import ru.otus.l11.polymorphism.Operation;

/**
 * @author sergey
 * created on 09.09.19.
 */
public class Update implements Operation {
  @Override
  public void action(String data) {
    System.out.println("update, data:" + data);
  }
}
