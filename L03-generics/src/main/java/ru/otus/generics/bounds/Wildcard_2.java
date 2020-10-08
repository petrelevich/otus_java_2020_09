package ru.otus.generics.bounds;

import java.util.ArrayList;
import java.util.List;
import ru.otus.generics.bounds.entries.Cat;

/**
 * @author sergey
 * created on 23.11.18.
 */
public class Wildcard_2 {

  public static void main(String[] args) {
    //
    //        List<Animal> animalList = new ArrayList<>();
    //        animalList.add(new Animal());
    //        printWild(animalList);

    List<Cat> catList = new ArrayList<>();
    catList.add(new Cat());
    //printWild(catList);
    printObj(catList);

    for (Cat cat : catList) {
      if (cat instanceof Cat) {
        System.out.println(cat.getMyau());
      }
    }


    //        //левый тип данных
    List<String> stringList = new ArrayList<>();
    stringList.add("подкидыш");
    //        printWild(stringList);
    //
    //        //Можно еще и так
    //        List voidList = new ArrayList<>();
    //        voidList.add(LocalTime.now());
    //        printWild(voidList);
    //        printObj(voidList);

  }

  //Unbounded Wildcards
  private static void printWild(List<?> animalList) {
    //animalList.add("внезапно добавленная строка"); //ошибка
    animalList.forEach(System.out::println);
  }

  private static void printObj(List animalList) {
    animalList.add("внезапно добавленная строка");
    animalList.forEach(System.out::println);
  }

}
