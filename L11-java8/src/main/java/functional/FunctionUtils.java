package functional;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

@UtilityClass
public class FunctionUtils {

  public <T, R> R map(T t, @NotNull Function<T, R> mapper) {
    return mapper.apply(t);
  }

  @Contract("_, _ -> param1")
  public <T> T peek(T t, @NotNull Consumer<T> consumer) {
    consumer.accept(t);
    return t;
  }

  @Contract("_, _ -> param1")
  public <T> T filter(T t, Predicate<T> predicate) {
    return filter(t, predicate, () -> new RuntimeException("filtered"));
  }

  @Contract("_, _, _ -> param1")
  public <T> T filter(T t, @NotNull Predicate<T> predicate, Supplier<? extends RuntimeException> throwableSupplier) {
    if (predicate.test(t))
      return t;
    else
      throw throwableSupplier.get();
  }

  @Contract("_, _, _ -> param1")
  public <T> T filterOrMap(T t, @NotNull Predicate<T> predicate, UnaryOperator<T> mapper) {
    return predicate.test(t) ? t : mapper.apply(t);
  }
}
