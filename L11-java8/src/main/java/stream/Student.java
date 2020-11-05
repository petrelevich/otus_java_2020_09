package stream;

import lombok.Builder;
import lombok.Value;

@Value
@Builder(toBuilder = true)
class Student {
  String name;

  @Builder.Default
  int age = 22;

  @Builder.Default
  int course = 5;

  double avgMark;
}
