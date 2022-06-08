package ru.otus.nio;

import java.nio.CharBuffer;

public class BufferExample {
    public static void main(String[] args) {
        new BufferExample().go();
    }

    private void go() {
        /* Буфферы можно делать как в хипе (это будут обычные Java instance), так и вне хипа для ускорения
         * производительности (привет unsafe'у из collection модуля) */
        var buffer = CharBuffer.allocate(10); //Создаем буфер для работы с char'ами
        System.out.printf("capacity: %d limit: %d position: %d%n", buffer.capacity(), buffer.limit(), buffer.position());

        //Строку преобразуем в массив char, чтобы получить источник данных для buffer'а
        var text = "testText".toCharArray();
        //Бежим по массиву char и по два char'а закидываем в buffer
        /*
            capacity: 10 limit: 10 position: 0
            idx: 0 capacity: 10 limit: 10 position: 2   - capacity и limit очевидно меняться не будет
            idx: 2 capacity: 10 limit: 10 position: 4   - а вот позиция увеличивается по мере записи
            idx: 4 capacity: 10 limit: 10 position: 6
            idx: 6 capacity: 10 limit: 10 position: 8
         */
        for (var idx = 0; idx < text.length; idx += 2) {
            buffer.put(text, idx, 2); //Длина 2 выбрана для учебного эффекта, на практике положили бы весь массив
            System.out.printf("idx: %d capacity: %d limit: %d position: %d %n", idx, buffer.capacity(), buffer.limit(), buffer.position());
        }

        //Для перевода режимов между read и write используется .flip()
        //Однако он не отслеживает текущий режим buffer'а, это должен делать программист.
        buffer.flip();

        //Мы не можем записать в buffer больше чем capacity. Когда мы записали, мы не можем прочитать больше,
        // чем записали (limit).
        /*
            idx: 0 char: t capacity: 10 limit:8 position: 1
            idx: 1 char: e capacity: 10 limit:8 position: 2
            idx: 2 char: s capacity: 10 limit:8 position: 3
            idx: 3 char: t capacity: 10 limit:8 position: 4
            idx: 4 char: T capacity: 10 limit:8 position: 5
            idx: 5 char: e capacity: 10 limit:8 position: 6
            idx: 6 char: x capacity: 10 limit:8 position: 7
            idx: 7 char: t capacity: 10 limit:8 position: 8
         */
        System.out.println("-----");
        for (var idx = 0; idx < buffer.limit(); idx++) {
            System.out.printf("idx: %d char: %s capacity: %d limit:%d position: %d %n", idx, buffer.get(), buffer.capacity(), buffer.limit(), buffer.position());
        }
    }
}
