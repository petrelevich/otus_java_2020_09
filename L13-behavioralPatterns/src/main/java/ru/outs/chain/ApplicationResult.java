package ru.outs.chain;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class ApplicationResult extends ApplProcessor {

  @Override
  protected void processInternal(Application application) {
    application.addHistoryRecord("Результат выдан");
  }

  @Override
  public String getProcessorName() {
    return "Выдача результата";
  }
}
