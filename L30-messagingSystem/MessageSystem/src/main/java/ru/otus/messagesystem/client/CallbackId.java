package ru.otus.messagesystem.client;

import java.io.Serializable;
import java.util.Objects;

public class CallbackId implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String id;

    public CallbackId(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return "CallbackId{" +
                "id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CallbackId that = (CallbackId) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
