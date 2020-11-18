package ru.otus.decorator;

/**
 * @author sergey
 * created on 16.01.19.
 */
public final class DataSourceImpl implements DataSource {
    @Override
    public int getInteger() {
        return 15;
    }
}
