package ru.otus.io;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;


public class DemoIO {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Каталог при повторении на ./demo
        System.out.println("current dir: " + System.getProperty("user.dir"));
        //copyFile("textFile.txt");
        //writeObject("person.bin");
        readObject("person.bin");
        //writeTextFile("textFile.txt");
        //readTextFile("textFile.txt");
    }

    //Задача: взять файл с диска, сделать его копию и запаковать.
    //Обратите внимание на цепочку декораторов
    private static void copyFile(String textFile) throws IOException {
        //Т.к. в нашу задачу входит прочитать файл с диска - берем FileInputStream(передаем имя файла/путь)
        //Далее чтение буферизируем. Если файл маленький, то толку в этом нет, однако это канонический пример
        //и делать следует сразу для файлов неизвестного объема. Можно передать второй параметр в конструктор буфера и
        //сделать его такого размера, которого он нужен.


        try (var bufferedInputStream = new BufferedInputStream(new FileInputStream(textFile));
             //Из bufferedInputStream мы будем извлекать данные. Теперь нам нужен выходной поток для записи, поэтому
             //используем FileOutputStream(указываем куда писать) и делаем его тоже буферизированным.
             //Т.к. мы хотим заархивировать файл используем еще и ZipOutputStream. Итог: матрешка из декораторов.
             var zipOut = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(textFile + "_copy.zip")))) {

            //теперь мы готовы брать данные из bufferedInputStream и передавать их в zipOut

            //Код ниже взят из документации к ZipOutputStream (две строки ниже и zipOut.closeEntry();)
            var zipEntry = new ZipEntry(textFile);
            zipOut.putNextEntry(zipEntry);

            //Все остальное - классическая работа со стримами.
            var buffer = new byte[2];
            int size;
            //Если мы что-то прочитали и это что-то больше нуля, то передаем в поток Output
            //Берем из bufferedInputStream, перекладываем в zipOut и перекладываем до тех пор, пока что-то читается
            while ((size = bufferedInputStream.read(buffer, 0, buffer.length)) > 0) {
                zipOut.write(buffer, 0, size);
            }
            zipOut.closeEntry();
        }
    }

    //Задача: сохранить на диск экземпляр класса
    private static void writeObject(String personFile) throws IOException {
        //Мы хотим сохранить экземпляр в файл, поэтому нам нужен FileOutputStream.
        //Чтобы донести объект до файла, нужна какая-то прослойка. Это - ObjectOutputStream
        try (var objectOutputStream = new ObjectOutputStream(new FileOutputStream(personFile))) {

            var person = new Person(92, "SerialPerson", "hidden");
            System.out.println("serializing:" + person);
            objectOutputStream.writeObject(person);
        }
    }

    //Задача: прочитать объект из файла
    private static void readObject(String personFile) throws IOException, ClassNotFoundException {
        //Для чтения объекта из файла стримы абсолютно зеркальны. Нам надо что-то прочитать, поэтому
        //используем FileInputStream, поскольку читаем Object - используем ObjectInputStream
        try (var objectInputStream = new ObjectInputStream(new FileInputStream(personFile))) {

            var person = (Person) objectInputStream.readObject();
            System.out.println("read object:" + person);
        }
    }

    //Задача: сохранить текст
    private static void writeTextFile(String textFile) throws IOException {
        var line1 = "Hello Java, str1";
        var line2 = "Hello Java, str2";
        try (var bufferedWriter = new BufferedWriter(new FileWriter(textFile))) {
            bufferedWriter.write(line1);
            bufferedWriter.newLine();
            bufferedWriter.write(line2);
        }
    }

    //Задача: прочитать текст
    private static void readTextFile(String textFile) throws IOException {
        try (var bufferedReader = new BufferedReader(new FileReader(textFile))) {
            System.out.println("text from the file:");
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }
}
