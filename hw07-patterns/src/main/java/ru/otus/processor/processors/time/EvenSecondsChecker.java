package ru.otus.processor.processors.time;

import static java.time.LocalTime.now;

public interface EvenSecondsChecker {
    default boolean currentSecondIsEven() {
        return now().getSecond() % 2 == 0;
    }
}
