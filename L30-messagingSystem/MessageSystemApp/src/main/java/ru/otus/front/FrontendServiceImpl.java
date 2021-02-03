package ru.otus.front;

import ru.otus.dto.UserData;
import ru.otus.messagesystem.client.MessageCallback;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageType;
import ru.otus.messagesystem.client.MsClient;


public class FrontendServiceImpl implements FrontendService {

    private final MsClient msClient;
    private final String databaseServiceClientName;

    public FrontendServiceImpl(MsClient msClient, String databaseServiceClientName) {
        this.msClient = msClient;
        this.databaseServiceClientName = databaseServiceClientName;
    }

    @Override
    public void getUserData(long userId, MessageCallback<UserData> dataConsumer) {
        Message outMsg = msClient.produceMessage(databaseServiceClientName, new UserData(userId),
                MessageType.USER_DATA, dataConsumer);
        msClient.sendMessage(outMsg);
    }
}
