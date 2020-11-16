package ru.otus.objectpool;

/**
 * @author sergey
 * created on 19.09.18.
 */
public class Demo {
  public static void main(String[] args) {
    ConnectionFactory connectionFactory =  new ConnectionFactory();
    ConnectionPool pool = new ConnectionPool(5,connectionFactory);
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select();
    pool.get().select();
  }
}
