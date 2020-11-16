package ru.otus.objectpool;

/**
 * @author sergey
 * created on 19.09.18.
 */
class ConnectionFactory {
  Connection create() {
    return new ConnectionOracle();
  }
}
