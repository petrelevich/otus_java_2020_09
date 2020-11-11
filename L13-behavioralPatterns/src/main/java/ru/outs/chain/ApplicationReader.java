package ru.outs.chain;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class ApplicationReader extends ApplProcessor {

  @Override
  protected void processInternal(Application application) {
    application.addHistoryRecord("Заявление рассмотрено");
  }

  @Override
  public String getProcessorName() {
    return "Рассмотрение заявления";
  }
}
