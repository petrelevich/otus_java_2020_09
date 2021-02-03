package ru.otus.messagesystem.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.otus.messagesystem.HandlersStore;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.MessageSystem;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.RequestHandler;

import java.util.Objects;

public class MsClientImpl implements MsClient {
    private static final Logger logger = LoggerFactory.getLogger(MsClientImpl.class);

    private final String name;
    private final MessageSystem messageSystem;
    private final HandlersStore handlersStore;
    private final CallbackRegistry callbackRegistry;

    public MsClientImpl(String name, MessageSystem messageSystem, HandlersStore handlersStore,
                        CallbackRegistry callbackRegistry) {
        this.name = name;
        this.messageSystem = messageSystem;
        this.handlersStore = handlersStore;
        this.callbackRegistry = callbackRegistry;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public boolean sendMessage(Message msg) {
        boolean result = messageSystem.newMessage(msg);
        if (!result) {
            logger.error("the last message was rejected: {}", msg);
        }
        return result;
    }

    @SuppressWarnings("all")
    @Override
    public void handle(Message msg) {
        logger.info("new message:{}", msg);
        try {
            RequestHandler requestHandler = handlersStore.getHandlerByType(msg.getType());
            if (requestHandler != null) {
                requestHandler.handle(msg).ifPresent(message -> sendMessage((Message) message));
            } else {
                logger.error("handler not found for the message type:{}", msg.getType());
            }
        } catch (Exception ex) {
            logger.error("msg:{}", msg, ex);
        }
    }

    @Override
    public <T extends ResultDataType> Message produceMessage(String to, T data, MessageType msgType,
                                                                MessageCallback<T> callback) {
        Message message = MessageBuilder.buildMessage(name, to, null, data, msgType);
        callbackRegistry.put(message.getCallbackId(), callback);
        return message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MsClientImpl msClient = (MsClientImpl) o;
        return Objects.equals(name, msClient.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }
}
