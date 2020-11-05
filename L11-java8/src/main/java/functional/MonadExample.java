package functional;

import java.util.Optional;

/**
 * @author sergey
 * created on 09.12.18.
 */
public class MonadExample {
  public static void main(String[] args) {
    var monadExample = new MonadExample();

    String result = monadExample.function("test");
    System.out.println(result);

    result = monadExample.function(null);
    System.out.println(result);

    result = monadExample.functionWrong(null);
    System.out.println(result);
  }

  private String function(String str) {
    Optional<String> optional = Optional.ofNullable(str);

    optional.stream().map(val -> "!" + val.toUpperCase()).forEach(System.out::println);

    return optional.map(param -> param + "+addStr").orElse("param is NULL");
  }

  //некорректное использование монады
  private String functionWrong(String str) {
    Optional<String> optional = Optional.ofNullable(str);
    if (optional.isPresent()) {
      return optional.get() + "+addStr";
    }
    return "param is NULL";
  }
}
