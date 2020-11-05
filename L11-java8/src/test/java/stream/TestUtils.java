//package stream;
//
//import java.io.ByteArrayOutputStream;
//import java.io.PrintStream;
//import java.util.function.Consumer;
//import lombok.Cleanup;
//
//
//
//public class TestUtils {
//
//  private final String LINE_SEPARATOR = System.lineSeparator();
//  private final String TEST_RESOURCES_PATH = "./src/test/resources/";
//
//  private static String fromPrintStream(Consumer<PrintStream> printStreamConsumer) {
//    var out = new ByteArrayOutputStream();
//    @Cleanup var printStream = new PrintStream(out);
//    printStreamConsumer.accept(printStream);
//    return new String(out.toByteArray()).intern();
//  }
//
//  public static String fromSystemOutPrint( Runnable task) {
//    return fromPrintStream(printStream -> {
//      PrintStream realOut = System.out;
//      System.setOut(printStream);
//      task.run();
//      System.setOut(realOut);
//    });
//  }
//
//
//  public static String fromSystemOutPrintln(Runnable runnable) {
//    String s = fromSystemOutPrint(runnable);
//    return s.endsWith(LINE_SEPARATOR) ?
//               s.substring(0, s.length() - LINE_SEPARATOR.length()) :
//               s;
//  }
//
//
//  @Contract(pure = true)
//  public String toTestResourceFilePath( String fileName) {
//    return TEST_RESOURCES_PATH + fileName;
//  }
//}
