package ru.otus.messagesystem.message;

public class MessageHelper {
    private MessageHelper() {
    }

    public static <T> T getPayload(Message msg) {
        return (T) Serializers.deserialize(msg.getPayload());
    }

    public static byte[] serializeMessage(Message msg) {
        return Serializers.serialize(msg);
    }

    public static Message deSerializeMessage(byte[] bytes) {
        return (Message) Serializers.deserialize(bytes);
    }
}
