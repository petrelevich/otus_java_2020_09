package ru.outs.chain;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class ApplicationInput extends ApplProcessor {

  @Override
  protected void processInternal(Application application) {
    application.addHistoryRecord("Заявление принято");
  }

  @Override
  public String getProcessorName() {
    return "Прием заявления";
  }
}
