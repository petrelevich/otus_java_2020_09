package ru.otus.messagesystem;

import ru.otus.messagesystem.client.ResultDataType;
import ru.otus.messagesystem.message.MessageType;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class HandlersStoreImpl implements HandlersStore {
    private final Map<String, RequestHandler<? extends ResultDataType>> handlers = new ConcurrentHashMap<>();

    @Override
    public RequestHandler<? extends ResultDataType> getHandlerByType(String messageTypeName) {
        return handlers.get(messageTypeName);
    }

    @Override
    public void addHandler(MessageType messageType, RequestHandler<? extends ResultDataType> handler) {
        handlers.put(messageType.getName(), handler);
    }


}
