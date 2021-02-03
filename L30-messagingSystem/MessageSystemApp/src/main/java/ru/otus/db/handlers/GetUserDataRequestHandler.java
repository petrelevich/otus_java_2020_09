package ru.otus.db.handlers;

import ru.otus.dto.UserData;
import ru.otus.messagesystem.message.MessageHelper;
import ru.otus.db.DBService;
import ru.otus.messagesystem.message.Message;
import ru.otus.messagesystem.message.MessageBuilder;
import ru.otus.messagesystem.RequestHandler;

import java.util.Optional;


public class GetUserDataRequestHandler implements RequestHandler<UserData> {
    private final DBService dbService;

    public GetUserDataRequestHandler(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    public Optional<Message> handle(Message msg) {
        UserData userData = MessageHelper.getPayload(msg);
        UserData data = new UserData(userData.getUserId(), dbService.getUserData(userData.getUserId()));
        return Optional.of(MessageBuilder.buildReplyMessage(msg, data));
    }
}
