package lambda;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.SneakyThrows;
import org.jetbrains.annotations.NotNull;

/**
 * author: Dmitry Arkhangelskiy
 * Изобретаем свои лямбды
 */
@SuppressWarnings({"WeakerAccess", "SameParameterValue"})
public class MapReduceExample {

  int x;

  static Double mySqrt(Integer val) {
    return Math.sqrt(val);
  }

  // Трансформация каждого элемента
  static <T, R> Collection<R> map(Collection<T> src, Function<T, R> mapper) {
    List<R> r = new ArrayList<>();
    for (T t : src) {
      r.add(mapper.apply(t));
    }
    return r;
  }

  static <T, R> R reduce(Collection<T> src, BiFunction<T, R, R> reducer, R zero) {
    R result = zero;
    for (T t : src) {
      result = reducer.apply(t, result);
    }
    return result;
  }

  static <R> Collection<R> filter(Collection<R> src, Predicate<R> predicate) {
    List<R> r = new ArrayList<>();
    for (R t : src) {
      if (predicate.test(t))
        r.add(t);
    }
    return r;
  }

  public static void main(String[] args) {
    List<Integer> list = List.of(4, 16, 25);
    System.out.println(map(list, new SquareRoot()));
    System.out.println(map(list, e -> Math.sqrt(e)));
    System.out.println(map(list, (Function<Integer, Double>) Math::sqrt));
    System.out.println(reduce(list, (v1, v2) -> v1 + v2, 0));
    System.out.println(filter(list, a -> a % 2 == 0));
    Function<Integer, Double> func1 = v -> mySqrt(v);
    // mySqrt() is Integer -> Double so it can be referenced as Function<Double, Integer>
    Function<Integer, Double> func2 = MapReduceExample::mySqrt;

    Function<String, Integer> function = String::hashCode;

  }

  @SneakyThrows
  public final @NotNull String m(MapReduceExample this) {
    System.out.println(this.x);
    return "";
  }

  // Integer -> Double
  static class SquareRoot implements Function<Integer, Double> {
    @Override
    public Double apply(Integer val) {
      return Math.sqrt(val);
    }
  }

}
