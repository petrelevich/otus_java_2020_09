//package stream;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//import java.util.Arrays;
//import java.util.Comparator;
//import java.util.List;
//import lombok.SneakyThrows;
//import org.jetbrains.annotations.NotNull;
//import org.junit.jupiter.api.DisplayName;
//import org.junit.jupiter.api.Test;
//
//class StreamsTest {
//
//  private final Streams streams = new Streams();
//
//  @Test
//  @DisplayName("Creating method works correctly")
//  void creating() {
//    assertThat(TestUtils.fromSystemOutPrintln(streams::creating))
//        .isEqualTo("10\n1231234");
//  }
//
//  @Test
//  @DisplayName("StringsJoiner method works correctly")
//  void stringsJoiner() {
//    assertThat(TestUtils.fromSystemOutPrintln(streams::stringsJoiner))
//        .isEqualTo("A,B,C,D\nA,B,C,D");
//  }
//
//  @Test
//  @DisplayName("FilterMapReduce method works correctly")
//  void filterMapReduce() {
//    assertThat(TestUtils.fromSystemOutPrintln(streams::filterMapReduce))
//        .isEqualTo("1\n10\n3\n30\n5\n50\n7\n70\n9\n90\nfilterMapReduce\nresult:250");
//  }
//
//  @Test
//  @SneakyThrows
//  @DisplayName("LinesReader method works correctly")
//  void linesReader() {
//    var uri = Streams.class.getResource("/lines.txt").toURI();
//    assertThat(TestUtils.fromSystemOutPrintln(() -> streams.linesReader(uri)))
//        .isEqualTo("65\tA-suff\n66\tB-suff\n67\tC-suff\n68\tD-suff");
//  }
//
//  @Test
//  @DisplayName("GetAvgMark method works correctly")
//  void getAvgMark() {
//    assertThat(streams.getAvgMark(getStudents()))
//        .isBetween(4.2, 4.3);
//  }
//
//  @NotNull
//  private List<Student> getStudents() {
//    var alex = Student.builder().name("Alex").avgMark(4.5).build();
//    var maria = Student.builder().name("Maria").avgMark(3.5).build();
//    var john = Student.builder().name("John").age(12).course(4).avgMark(4.7).build();
//    var bob = Student.builder().name("Bob").avgMark(4.8).build();
//    var anna = Student.builder().name("Anna").age(20).course(3).avgMark(4.5).build();
//    return List.of(alex, maria, john, bob, anna);
//  }
//
//  @Test
//  @DisplayName("GroupBy method works correctly")
//  void groupBy() {
//    assertThat(TestUtils.fromSystemOutPrintln(() -> streams.groupBy(getStudents())))
//        .isEqualTo(
//            "{" +
//                "3=[Student(name=Anna, age=20, course=3, avgMark=4.5)], " +
//                "4=[Student(name=John, age=12, course=4, avgMark=4.7)], " +
//                "5=[Student(name=Alex, age=22, course=5, avgMark=4.5), " +
//                "Student(name=Maria, age=22, course=5, avgMark=3.5), " +
//                "Student(name=Bob, age=22, course=5, avgMark=4.8)]" +
//                "}");
//  }
//
//  @Test
//  @DisplayName("lorem")
//  void loremTest() {
//    //                           .map(String::length)
//    Arrays.stream("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum."
//                      .split(" "))
//        .max(Comparator.comparingInt(String::length))
//        .ifPresent(System.out::println);
//  }
//}
