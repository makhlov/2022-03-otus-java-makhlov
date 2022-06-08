package ru.otus.nio;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousFileChannel;
import java.nio.channels.CompletionHandler;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.TimeUnit;

/* Вот боевой пример:
https://github.com/gridgain/gridgain/blob/d6222c6d892eabcbcfc60fd75fc2d38a7dd06bb6/modules/core/src/main/java/org/apache/ignite/internal/processors/cache/persistence/file/AsyncFileIO.java
 Ниже максимально упрощенный без углубления в вопросы потокобезопасности и прочего
 */

public class AsyncRead implements AutoCloseable {
    //Задача: прочитать файл в несколько терабайт без блокировки
    //Если пользоваться старым IO, то мы откроем файл, начнем его лопатить, а в это время процесс будет простаивать:
    //поток на стороне Java работает дискретно - обращается к устройству IO, читает данные, и возвращает их в поток.
    //Пока файл читается с источника поток будет простаивать. Можно этот поток занять полезным делом. При этом пользователь
    //может решить, что файл ему не нужен и прервать чтение. Если читаем полностью, то сделать это будет сложнее

    //Поэтому можно читать файл асинхронно. Читаем, получаем уведомление, что что-то прочитали, читаем дальше
    //Пока читается, можно заняться чем-то полезным
    private final ByteBuffer buffer = ByteBuffer.allocate(2);
    private final AsynchronousFileChannel fileChannel;
    private final StringBuilder fileContent = new StringBuilder();

    private final CompletionHandler<Integer, ByteBuffer> completionHandler =
            //Мы говорим NIO - читай кусочек файла, а когда прочитаешь - запускай этот метод
            new CompletionHandler<>() {
                private int lastPosition = 0;

                //Когда читается какой-то фрагмент файла, вызывается функция complete
                @Override
                public void completed(Integer readBytes, ByteBuffer attachment) {
                    System.out.println(Thread.currentThread().getName() + ". readBytes:" + readBytes);
                    byte[] destArray;
                    //Начинаем с этими байтами что-то делать - складываем в промежуточный буффер (в данном случае
                    //это StringBuilder
                    if (readBytes > 0) {
                        destArray = new byte[readBytes];
                        attachment.flip(); //Перекладываем данные в buffer
                        attachment.get(destArray, 0, destArray.length);

                        lastPosition += readBytes; //Двигаем позицию..
                        read(fileChannel, lastPosition, buffer); //И читаем еще одну порцию
                        fileContent.append(new String(destArray)); //То что уже прочитано складываем в контент
                    } else {
                        System.out.println(Thread.currentThread().getName() + " fileData:\n" + fileContent);
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

    //handler вызывается read
    private void read() throws InterruptedException {
        //Мы начинаем операцию чтения и не дожидаемся ее завершения
        fileChannel.read(buffer, 0, buffer, completionHandler);

        System.out.println(Thread.currentThread().getName() + " Hello");

        Thread.sleep(TimeUnit.SECONDS.toMillis(1));
    }

    @Override
    public void close() throws Exception {
        fileChannel.close();
    }
}
