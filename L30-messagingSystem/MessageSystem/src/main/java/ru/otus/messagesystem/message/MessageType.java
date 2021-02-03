package ru.otus.messagesystem.message;

public enum MessageType {
    USER_DATA("UserData");

    private final String name;

    MessageType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
