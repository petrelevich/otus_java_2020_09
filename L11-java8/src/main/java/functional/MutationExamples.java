package functional;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author sergey
 * created on 09.12.18.
 */

public class MutationExamples {

  private List<TestObjectMutable> listMute = Arrays.asList(
      new TestObjectMutable(1),
      new TestObjectMutable(2),
      new TestObjectMutable(3));

  private List<TestObjectUnMutable> listUnMute = Arrays.asList(
      new TestObjectUnMutable(1),
      new TestObjectUnMutable(2),
      new TestObjectUnMutable(3));


  public static void main(String[] args) {
    new MutationExamples().mutableUnMutable();
  }

  private void mutableUnMutable() {
    //Собираем новую коллекцию из старой
    //Очень плохая идея - менять элементы исходной коллекции
    List<TestObjectMutable> newList = listMute.stream()
                                          .map(elem -> elem.updateValue(-1))
                                          .collect(Collectors.toList());
    System.out.println(newList);

    //Лучше так - создаем новый экземпляр, если "надо бы поменять существующий"
    List<TestObjectUnMutable> newList2 = listUnMute.parallelStream()
                                             .map(elem -> elem.updateValue(-1))
                                             .collect(Collectors.toList());
    System.out.println(newList2);
  }
}
