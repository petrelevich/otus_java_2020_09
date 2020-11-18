package ru.otus.bridge;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class VisaPS implements PaymentSystem {
    @Override
    public void printName() {
        System.out.println("VisaPS");
    }
}
