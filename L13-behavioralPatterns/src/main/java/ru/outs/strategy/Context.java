package ru.outs.strategy;

/**
 * @author sergey
 * created on 11.09.18.
 */
class Context {
    private Strategy strategy;

    void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    void applyStrategy() {
        strategy.transportation();
    }
}
