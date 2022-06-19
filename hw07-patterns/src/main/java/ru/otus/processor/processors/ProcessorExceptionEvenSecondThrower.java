package ru.otus.processor.processors;

import ru.otus.model.Message;
import ru.otus.processor.Processor;
import ru.otus.processor.exception.EvenSecondException;
import ru.otus.processor.processors.time.EvenSecondsHelper;

import java.time.LocalDateTime;

public class ProcessorExceptionEvenSecondThrower implements Processor {
    private final EvenSecondsHelper secondsHelper;

    public ProcessorExceptionEvenSecondThrower(EvenSecondsHelper secondsHelper) {
        this.secondsHelper = secondsHelper;
    }

    @Override
    public Message process(Message message) {
        if (secondsHelper.currentSecondIsEven()) throw new EvenSecondException();
        return message;
    }
}