package ru.otus.bridge;

/**
 * @author sergey
 * created on 16.01.19.
 */
public abstract class Card {
    private final PaymentSystem paymentSystem;

    public Card(PaymentSystem paymentSystem) {
        this.paymentSystem = paymentSystem;
    }

    public void info() {
        paymentSystem.printName();
        cardType();
    }

    protected abstract void cardType();
}
