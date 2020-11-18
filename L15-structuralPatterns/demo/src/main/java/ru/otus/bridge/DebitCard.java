package ru.otus.bridge;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class DebitCard extends Card {
    public DebitCard(PaymentSystem paymentSystem) {
        super(paymentSystem);
    }

    @Override
    protected void cardType() {
        System.out.println("debit card");
    }
}
