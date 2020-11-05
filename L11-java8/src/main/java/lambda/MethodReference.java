package lambda;

import java.util.function.IntSupplier;

public class MethodReference {

  Finder finder1 = String::indexOf;
  Finder finder2 = (s1, s2) -> s1.indexOf(s2);

  public static void main(String... __) {

    int x = 6;

    IntSupplier intSupplier = () -> 56 + x;

    System.out.println(intSupplier.getAsInt());
    //    x = 5;
  }

  public interface Finder {
    int find(String s1, String s2);
  }
}
