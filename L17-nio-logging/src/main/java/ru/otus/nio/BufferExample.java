package ru.otus.nio;

import java.nio.CharBuffer;

/**
 * @author sergey
 * created on 25.09.18.
 */
public class BufferExample {
    public static void main(String[] args) {
        new BufferExample().go();
    }

    private void go() {
        CharBuffer buffer = CharBuffer.allocate(10);
        System.out.println("capacity:" + buffer.capacity() + " limit:" + buffer.limit() + " position:" + buffer.position());

        char[] text = "testText".toCharArray();
        for (int idx = 0; idx < text.length; idx += 2) {
            buffer.put(text, idx, 2);
            System.out.println("idx:" + idx + " capacity:" + buffer.capacity() + " limit:" + buffer.limit() + " position:" + buffer.position());
        }

        buffer.flip();

        System.out.println("-----");
        for (int idx = 0; idx < buffer.limit(); idx++) {
            System.out.println("idx:" + idx
                    + " char:" + buffer.get()
                    + " capacity:" + buffer.capacity()
                    + " limit:" + buffer.limit()
                    + " position:" + buffer.position());
        }
    }
}
