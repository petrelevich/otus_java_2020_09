package ru.otus.messagesystem;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.RepeatedTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.db.DBService;
import ru.otus.db.handlers.GetUserDataRequestHandler;
import ru.otus.front.FrontendService;
import ru.otus.front.FrontendServiceImpl;
import ru.otus.front.handlers.GetUserDataResponseHandler;
import ru.otus.messagesystem.client.CallbackRegistry;
import ru.otus.messagesystem.client.CallbackRegistryImpl;
import ru.otus.messagesystem.client.MsClient;
import ru.otus.messagesystem.client.MsClientImpl;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;

import java.util.concurrent.CountDownLatch;
import java.util.stream.IntStream;
import java.util.stream.LongStream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class IntegrationTest {
    private static final Logger logger = LoggerFactory.getLogger(IntegrationTest.class);

    private static final String FRONTEND_SERVICE_CLIENT_NAME = "frontendService";
    private static final String DATABASE_SERVICE_CLIENT_NAME = "databaseService";

    private MessageSystem messageSystem;
    private FrontendService frontendService;
    private MsClient frontendMsClient;

    @DisplayName("Базовый сценарий получения данных")
    @RepeatedTest(1000)
    void getDataById() throws Exception {
        createMessageSystem(true);
        int counter = 3;
        CountDownLatch waitLatch = new CountDownLatch(counter);

        LongStream.range(0, counter).forEach(id ->
                frontendService.getUserData(id, data -> {
                    assertThat(data.getUserId()).isEqualTo(id);
                    waitLatch.countDown();
                }));

        waitLatch.await();
        messageSystem.dispose();
        logger.info("done");
    }

    @DisplayName("Выполнение запроса после остановки сервиса")
    @RepeatedTest(2)
    void getDataAfterShutdown() throws Exception {
        createMessageSystem(true);
        messageSystem.dispose();

        CountDownLatch waitLatchShutdown = new CountDownLatch(1);

        when(frontendMsClient.sendMessage(any(Message.class))).
                thenAnswer(invocation -> {
                    waitLatchShutdown.countDown();
                    return null;
                });

        frontendService.getUserData(5, data -> logger.info("data:{}", data));
        waitLatchShutdown.await();
        boolean result = verify(frontendMsClient).sendMessage(any(Message.class));
        assertThat(result).isFalse();

        logger.info("done");
    }

    @DisplayName("Тестируем остановку работы MessageSystem")
    @RepeatedTest(1000)
    void stopMessageSystem() throws Exception {
        createMessageSystem(false);
        int counter = 100;
        CountDownLatch messagesSentLatch = new CountDownLatch(counter);
        CountDownLatch messageSystemDisposed = new CountDownLatch(1);

        IntStream.range(0, counter).forEach(id -> {
                    frontendService.getUserData(id, data -> {});
                    messagesSentLatch.countDown();
                }
        );
        messagesSentLatch.await();
        assertThat(messageSystem.currentQueueSize()).isEqualTo(counter);

        messageSystem.start();
        disposeMessageSystem(messageSystemDisposed::countDown);

        messageSystemDisposed.await();
        assertThat(messageSystem.currentQueueSize()).isZero();

        logger.info("done");
    }


    private void createMessageSystem(boolean startProcessing) {
        logger.info("setup");
        messageSystem = new MessageSystemImpl(startProcessing);
        CallbackRegistry callbackRegistry = new CallbackRegistryImpl();

        DBService dbService = mock(DBService.class);
        when(dbService.getUserData(any(Long.class))).thenAnswer(invocation -> String.valueOf((Long) invocation.getArgument(0)));

        HandlersStore requestHandlerDatabaseStore = new HandlersStoreImpl();
        requestHandlerDatabaseStore.addHandler(MessageType.USER_DATA, new GetUserDataRequestHandler(dbService));
        MsClient databaseMsClient = new MsClientImpl(DATABASE_SERVICE_CLIENT_NAME, messageSystem,
                requestHandlerDatabaseStore, callbackRegistry);
        messageSystem.addClient(databaseMsClient);

        //////////////////////////
        HandlersStore requestHandlerFrontendStore = new HandlersStoreImpl();
        requestHandlerFrontendStore.addHandler(MessageType.USER_DATA, new GetUserDataResponseHandler(callbackRegistry));

        frontendMsClient = spy(new MsClientImpl(FRONTEND_SERVICE_CLIENT_NAME, messageSystem,
                requestHandlerFrontendStore, callbackRegistry));
        frontendService = new FrontendServiceImpl(frontendMsClient, DATABASE_SERVICE_CLIENT_NAME);
        messageSystem.addClient(frontendMsClient);

        logger.info("setup done");
    }

    private void disposeMessageSystem(Runnable callback) {
        try {
            messageSystem.dispose(callback);
        } catch (InterruptedException ex) {
            logger.error(ex.getMessage(), ex);
        }
    }
}
