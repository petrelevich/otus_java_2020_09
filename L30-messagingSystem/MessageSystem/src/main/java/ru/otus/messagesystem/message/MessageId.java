package ru.otus.messagesystem.message;

import java.io.Serializable;
import java.util.Objects;

public class MessageId implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;

    public MessageId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "MessageId{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageId messageId = (MessageId) o;
        return Objects.equals(id, messageId.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
