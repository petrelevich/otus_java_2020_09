package functional;

/**
 * @author sergey
 * created on 09.12.18.
 */
public final class TestObjectUnMutable {
  public final int value;

  public TestObjectUnMutable(int value) {
    this.value = value;
  }

  public TestObjectUnMutable updateValue(int value) {
    return new TestObjectUnMutable(value);
  }


  @Override
  public String toString() {
    return "TestObjectUnMutable{" +
               "value=" + value +
               '}';
  }
}
