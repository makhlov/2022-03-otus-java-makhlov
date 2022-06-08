package ru.otus.nio;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CopyFiles {
    //Задача: скопировать файл asap
    public static void main(String[] args) throws IOException {
        copyFiles("textFile.txt", "textFile.bak");
    }

    //У нас есть файл источник, файл назначения (как правило, в несколько терабайт)
    private static void copyFiles(String srcFile, String destFile) throws IOException {
        //Для быстрого копирования надо копировать на уровне системы, минуя посредника в виде Java
        //Потоки IO будут скрещены не на уровне Java а на уровне файловой системы
        //Однако, если этот вид копирования не поддерживается, то копирование будет выполнено на стороне Java,
        //т.к. Java должна работать на любых ОС
        try (var channelSrc = FileChannel.open(Path.of(srcFile), StandardOpenOption.READ);
             var channelDest = FileChannel.open(Path.of(destFile), StandardOpenOption.CREATE, StandardOpenOption.WRITE)) {

            channelSrc.transferTo(0, channelSrc.size(), channelDest);
            //Старый IO такое позволить не мог
        }
    }
}
