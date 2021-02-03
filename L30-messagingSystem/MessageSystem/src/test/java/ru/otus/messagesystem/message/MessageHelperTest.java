package ru.otus.messagesystem.message;

import org.junit.jupiter.api.Test;
import ru.otus.messagesystem.client.CallbackId;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class MessageHelperTest {

    @Test
    void serializeDeSerializeMessage() {
        Message msgExpected = new Message(new MessageId(UUID.randomUUID().toString()), "from", "to",
                new MessageId(UUID.randomUUID().toString()), "type",
                new byte[12], new CallbackId(UUID.randomUUID().toString()));

        byte[] data = MessageHelper.serializeMessage(msgExpected);

        Message msg = MessageHelper.deSerializeMessage(data);

        assertThat(msg.getId()).isEqualTo(msgExpected.getId());
        assertThat(msg.getFrom()).isEqualTo(msgExpected.getFrom());
        assertThat(msg.getTo()).isEqualTo(msgExpected.getTo());
        assertThat(msg.getSourceMessageId()).isEqualTo(msgExpected.getSourceMessageId());
        assertThat(msg.getType()).isEqualTo(msgExpected.getType());
        assertThat(msg.getPayload()).isEqualTo(msgExpected.getPayload());
        assertThat(msg.getCallbackId()).isEqualTo(msgExpected.getCallbackId());
    }
}