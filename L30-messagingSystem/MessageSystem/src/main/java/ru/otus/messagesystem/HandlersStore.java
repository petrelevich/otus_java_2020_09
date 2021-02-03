package ru.otus.messagesystem;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public interface HandlersStore {
    RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName);

    void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler);
}
