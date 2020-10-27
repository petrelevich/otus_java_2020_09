package ru.otus;

/*
java -Xdebug -Xrunjdwp:transport=dt_socket,address=5005,server=y,suspend=n -jar remoteDebugDemo-0.1.jar
*/
public class RemoteDebug {

    private int value = 0;

    public static void main(String[] args) throws InterruptedException {
        new RemoteDebug().loop();
    }

    private void loop() throws InterruptedException {
        while (!Thread.currentThread().isInterrupted()) {
            value += 1_000;
            incVal();
            System.out.println(value);
            Thread.sleep(2_000);
        }
    }

    private void incVal() {
        value += 16;
    }
}
