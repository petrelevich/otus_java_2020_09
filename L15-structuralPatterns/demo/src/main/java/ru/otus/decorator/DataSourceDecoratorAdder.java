package ru.otus.decorator;

/**
 * @author sergey
 * created on 16.01.19.
 */
public class DataSourceDecoratorAdder implements DataSource {
    private final DataSource dataSource;

    public DataSourceDecoratorAdder(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Override
    public int getInteger() {
        return dataSource.getInteger() + 10;
    }
}
