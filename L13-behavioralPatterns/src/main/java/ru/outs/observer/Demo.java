package ru.outs.observer;

/**
 * @author sergey
 * created on 11.09.18.
 */
public class Demo {
  public static void main(String[] args) {
    EventProducer producer = new EventProducer();
    ConsumerA consumerA = new ConsumerA();
    ConsumerB consumerB = new ConsumerB();

    producer.addListener(consumerA);
    producer.addListener(consumerB.getListener());

    producer.event("eventA");
    producer.event("eventB");

    //Критически важно удалять, когда не нужны
    producer.removeListener(consumerA);
    producer.removeListener(consumerB.getListener());

    producer.event("eventC");
    producer.event("eventD");
  }
}
