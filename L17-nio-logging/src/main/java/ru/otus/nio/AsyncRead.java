package ru.otus.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/*
https://github.com/gridgain/gridgain/blob/d6222c6d892eabcbcfc60fd75fc2d38a7dd06bb6/modules/core/src/main/java/org/apache/ignite/internal/processors/cache/persistence/file/AsyncFileIO.java
 */

public class AsyncRead implements AutoCloseable {
    private final ByteBuffer buffer = ByteBuffer.allocate(2);
    private final AsynchronousFileChannel fileChannel;
    private final StringBuilder fileContent = new StringBuilder();

    private final CompletionHandler<Integer, ByteBuffer> completionHandler =
            new CompletionHandler<>() {
                private int lastPosition = 0;

                @Override
                public void completed(Integer readBytes, ByteBuffer attachment) {
                    System.out.println("readBytes:" + readBytes);
                    byte[] destArray;
                    if (readBytes > 0) {
                        destArray = new byte[readBytes];
                        attachment.flip();
                        attachment.get(destArray, 0, destArray.length);

                        lastPosition += readBytes;
                        read(fileChannel, lastPosition, buffer);
                        fileContent.append(new String(destArray));
                    } else {
                        System.out.println("fileData:\n" + fileContent);
                    }
                }

                @Override
                public void failed(Throwable exc, ByteBuffer attachment) {
                    System.err.println("error:" + exc.getMessage());
                }

                private void read(AsynchronousFileChannel fileChannel, int position, ByteBuffer buffer) {
                    buffer.clear();
                    fileChannel.read(buffer, position, buffer, completionHandler);
                }
            };


    public static void main(String[] args) throws Exception {
        try (var asyncRead = new AsyncRead()) {
            asyncRead.read();
        }
    }

    public AsyncRead() throws IOException {
        fileChannel = AsynchronousFileChannel.open(Path.of("textFile.txt"), StandardOpenOption.READ);
    }

    private void read() throws InterruptedException {
        fileChannel.read(buffer, 0, buffer, completionHandler);

        System.out.println("Hello");

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
    }
}
