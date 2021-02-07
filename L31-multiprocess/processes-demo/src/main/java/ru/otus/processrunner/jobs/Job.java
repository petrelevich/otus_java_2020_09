package ru.otus.processrunner.jobs;

import java.util.stream.IntStream;

/*
from location: src/main/java
javac ru/otus/processrunner/jobs/Job.java
java ru.otus.processrunner.jobs.Job
 */
public class Job {
  public static void main(String[] args) {
    String endOfRangeEnvVar = System.getenv("endOfRange");

    System.out.printf("EndOfRange environment variable: %s\n", endOfRangeEnvVar);
    int endOfRange = endOfRangeEnvVar == null? 100: Integer.parseInt(endOfRangeEnvVar);
    IntStream.range(1, endOfRange).forEach(System.out::println);
  }
}
