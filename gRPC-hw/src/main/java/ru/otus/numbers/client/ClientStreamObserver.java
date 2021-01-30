package ru.otus.numbers.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.numbers.NumberResponse;

public class ClientStreamObserver implements io.grpc.stub.StreamObserver<ru.otus.numbers.NumberResponse> {
    private static final Logger log = LoggerFactory.getLogger(ClientStreamObserver.class);

    private long lastValue = 0;

    @Override
    public void onNext(NumberResponse numberResponse) {
        log.info("new value:{}", numberResponse.getNumber());
        setLastValue(numberResponse.getNumber());
    }

    @Override
    public void onError(Throwable e) {
        log.error("got error", e);
    }

    @Override
    public void onCompleted() {
        log.info("request completed");
    }

    private synchronized void setLastValue(long value) {
        this.lastValue = value;
    }

    public synchronized long getLastValueAndReset() {
        var lastValuePrev = this.lastValue;
        this.lastValue = 0;
        return lastValuePrev;
    }
}
