package ru.otus.nio;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Paths;

public class ChannelExample {
    public static void main(String[] args) throws IOException, URISyntaxException {
        new ChannelExample().go();
    }

    //Задача: есть файл, который лежит в ресурсах: resources/share.xml
    //Мы хотим эту xml прочитать.
    private void go() throws IOException, URISyntaxException {
        //Ставим указатель на файл и открываем его...
        var path = Paths.get(ClassLoader.getSystemResource("share.xml").toURI());
        //Передаем Path в FileChannel (мы работаем с файлом, нам нужен FileChannel)
        try (var fileChannel = FileChannel.open(path)) {
            //Мы будем извлекать данные с FileChannel в buffer на 10 эл.
            var buffer = ByteBuffer.allocate(10);

            //Поскольку результаты мы хотим вывести в консоль, промежуточный result будем накапливать в
            //StringBuilder.
            int bytesCount;
            var sb = new StringBuilder();
            do {
                //Обращаемся к fileChannel и читаем
                bytesCount = fileChannel.read(buffer);
                //Прочитали порцию - переворачиваем для извлечения все что начитали из buffer в StringBuilder
                buffer.flip();
                while (buffer.hasRemaining()) {
                    sb.append((char) buffer.get());
                }

                //Для продолжения записи переворачиваем обратно
                buffer.flip();
            } while (bytesCount > 0); //Цикл работает до тех пор, пока что-то прочитали

            //Байты кончились - выводим полученную информацию на экран
            System.out.println(sb);
        }
    }
}
