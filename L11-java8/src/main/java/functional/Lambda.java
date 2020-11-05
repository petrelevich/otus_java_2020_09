package functional;

import java.util.function.Function;
import java.util.function.Supplier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

/**
 * @author sergey
 * created on 09.12.18.
 */
public class Lambda {

  private String value;

  public static void main(String[] args) {
    Lambda lambda = new Lambda();

    String result = lambda.func(str -> str + "+mod", "testStr");
    System.out.println(result);

    Integer result2 = lambda.func(val -> val * 10, 5);
    System.out.println(result2);

    // "Билдер" экземпляров Lambda с инициализацией поля value константой
    Lambda l = lambda.func(lb -> {
      lb.value = "testValue";
      return lb;
    }, new Lambda());
    System.out.println(l.value);

    //int[] initValue - не поле инстанса или класса, но сохраняет свое значение между вызовами функции
    Supplier<Integer> closure = lambda.generator();
    System.out.println("1:" + closure.get());
    System.out.println("2:" + closure.get());
    System.out.println("3:" + closure.get());
  }


  private <T, R> R func(@NotNull Function<T, R> func, T param) {
    return func.apply(param);
  }

  @NotNull
  @Contract(pure = true)
  private Supplier<Integer> generator() {
    int[] initValue = {0}; //Переменная не только effectively final, но и effectively private :)
    return () -> ++initValue[0];
  }
}
