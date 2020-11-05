package stream;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;
import lombok.AllArgsConstructor;
import lombok.Cleanup;
import lombok.SneakyThrows;

@AllArgsConstructor
public class Streams {

  void creating() {
    Stream<String> empty = Stream.empty();
    empty.forEach(System.out::println);

    Stream<Integer> single = Stream.of(10);
    single.forEach(System.out::println);

    Stream<Integer> array = Stream.of(1, 2, 3);
    array.forEach(System.out::print);

    IntStream range = IntStream.range(1, 5);
    range.forEach(System.out::print);
  }

  void stringsJoiner() {
    String[] arrayOfString = {"A", "B", "C", "D"};

    String result = Stream.of(arrayOfString)
                        .collect(Collectors.joining(","));
    System.out.println(result);

    String result2 = String.join(",", arrayOfString);
    System.out.println(result2);
  }

  void filterMapReduce() {
    System.out.println(
        "filterMapReduce\nresult:"
            + List.of(1, 2, 3, 4, 5, 6, 7, 8, 9).stream()
                  .filter(val -> val % 2 > 0)
                  .peek(System.out::println)
                  .map(val -> val * 10)
                  .peek(System.out::println)
                  .reduce(0, Integer::sum));
  }

  @SneakyThrows
  void linesReader(URI uri) {
    @Cleanup Stream<String> lines = Files.lines(Paths.get(uri));
    lines
        .map(String::trim)
        .filter(str -> !str.isEmpty())
        .sorted(Comparator.comparingLong(String::hashCode))
        .map(line -> String.format("%d\t%s-suff", line.hashCode(), line))
        .forEach(System.out::println);
  }

  double getAvgMark(List<Student> students) {
    return students.stream()
               .filter(st -> st.getCourse() == 5)
               .mapToDouble(Student::getAvgMark)
               .average()
               .orElse(0.0);
  }

  void groupBy(List<Student> students) {
    System.out.println(students.stream()
                           .collect(Collectors.groupingBy(Student::getCourse)));
  }
}
