package ru.otus.numbers.client;

import io.grpc.ManagedChannelBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.numbers.NumberRequest;
import ru.otus.numbers.NumbersServiceGrpc;


import static ru.otus.numbers.ApplicationProperties.getServerHost;
import static ru.otus.numbers.ApplicationProperties.getServerPort;

public class NumbersClient {
    private static final Logger log = LoggerFactory.getLogger(NumbersClient.class);
    private static final int LOOP_LIMIT = 50;

    private long value = 0;

    public static void main(String[] args) {
        log.info("numbers Client is starting...");

        var managedChannel = ManagedChannelBuilder.forAddress(getServerHost(), getServerPort())
                .usePlaintext()
                .build();

        var asyncClient = NumbersServiceGrpc.newStub(managedChannel);

        new NumbersClient().clientAction(asyncClient);

        log.info("numbers Client is shutting down...");
        managedChannel.shutdown();
    }

    private void clientAction(NumbersServiceGrpc.NumbersServiceStub asyncClient) {
        var numberRequest = makeNumberRequest();
        var clientStreamObserver = new ClientStreamObserver();
        asyncClient.number(numberRequest, clientStreamObserver);

        for(var idx = 0; idx < LOOP_LIMIT; idx++) {
            var valForPrint = getNextValue(clientStreamObserver);
            log.info("currentValue:{}", valForPrint);
            sleep();
        }
    }

    private long getNextValue(ClientStreamObserver clientStreamObserver) {
        value = value + clientStreamObserver.getLastValueAndReset() + 1;
        return value;
    }

    private NumberRequest makeNumberRequest() {
        return NumberRequest.newBuilder()
                .setFirstValue(1)
                .setLastValue(10)
                .build();
    }

    private static void sleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
