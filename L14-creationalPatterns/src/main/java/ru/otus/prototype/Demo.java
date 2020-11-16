package ru.otus.prototype;

/**
 * @author sergey
 * created on 19.09.18.
 */
public class Demo {
  public static void main(String[] args) throws CloneNotSupportedException {
    copyExample();
    //cloneExample();
  }

  private static void copyExample() {
    System.out.println("======== copyExample ========");
    Sheep original = new Sheep("unknown");
    System.out.println(original);

    Sheep cloned = original.copy();

    System.out.println(original.equals(cloned));

    cloned.setName("Dolly");
    System.out.println(cloned);

    System.out.println(original.equals(cloned));
  }

  /**
   * Стандартный clone()
   * @throws CloneNotSupportedException
   */
  private static void cloneExample() throws CloneNotSupportedException {
    System.out.println("======== cloneExample ========");

    ClonableSheep original = new ClonableSheep("unknown");
    System.out.println(original);

    ClonableSheep cloned = original.clone();

    System.out.println(original.equals(cloned));

    cloned.setName("Dolly");
    System.out.println(cloned);

    System.out.println(original.equals(cloned));
  }


}
