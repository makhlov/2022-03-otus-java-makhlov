package ru.otus.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class DemoSerialization {
    public static void main(String[] args) throws IOException, ClassNotFoundException {
        //Сделаем сериализацию и десериализацию - передадим Person из одного метода в другой.
        deserialize(serialize());
    }

    //Преобразуем Person в массив байт
    private static byte[] serialize() throws IOException {
        try (var byteArrayOutputStream = new ByteArrayOutputStream();
             var objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)
        ) {
            var person = new Person(12, "SerialPersonForArray", "value");
            System.out.println("Serializing:" + person);
            objectOutputStream.writeObject(person);
            return byteArrayOutputStream.toByteArray();
        }
    }

    //Передаем массив байт и на выходе получаем экземпляр класса Person
    private static void deserialize(byte[] data) throws IOException, ClassNotFoundException {
        try (var objectInputStream = new ObjectInputStream(new ByteArrayInputStream(data))) {
            var person = (Person) objectInputStream.readObject();
            System.out.println("deSerialized person:" + person);
        }
    }
}
