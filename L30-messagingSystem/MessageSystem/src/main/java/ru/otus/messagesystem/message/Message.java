package ru.otus.messagesystem.message;

import ru.otus.messagesystem.client.CallbackId;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;

public class Message implements Serializable {
    private static final long serialVersionUID = 1L;

    private final MessageId id;
    private final String from;
    private final String to;
    private final MessageId sourceMessageId;
    private final String type;
    private final byte[] payload;
    private final CallbackId callbackId;

    Message(MessageId messageId, String from, String to, MessageId sourceMessageId, String type,
            byte[] payload, CallbackId callbackId) {
        this.id = messageId;
        this.from = from;
        this.to = to;
        this.sourceMessageId = sourceMessageId;
        this.type = type;
        this.payload = payload;
        this.callbackId = callbackId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Message message = (Message) o;
        return Objects.equals(id, message.id) &&
                Objects.equals(from, message.from) &&
                Objects.equals(to, message.to) &&
                Objects.equals(sourceMessageId, message.sourceMessageId) &&
                Objects.equals(type, message.type) &&
                Arrays.equals(payload, message.payload) &&
                Objects.equals(callbackId, message.callbackId);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(id, from, to, sourceMessageId, type, callbackId);
        result = 31 * result + Arrays.hashCode(payload);
        return result;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", from='" + from + '\'' +
                ", to='" + to + '\'' +
                ", sourceMessageId=" + sourceMessageId +
                ", type='" + type + '\'' +
                ", callbackId=" + callbackId +
                '}';
    }

    public MessageId getId() {
        return id;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public String getType() {
        return type;
    }

    public byte[] getPayload() {
        return payload;
    }

    public CallbackId getCallbackId() {
        return callbackId;
    }

    public Optional<MessageId> getSourceMessageId() {
        return Optional.ofNullable(sourceMessageId);
    }
}
