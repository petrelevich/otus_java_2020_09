package ru.otus.singleton;

public class SingletonJdkDemo {
    public static void main(String[] args) {
        Runtime runtime = Runtime.getRuntime();

        System.out.println("freeMemory = " + runtime.freeMemory());
    }
}
