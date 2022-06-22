package ru.otus.processor.processors;

import ru.otus.model.Message;
import ru.otus.processor.Processor;

public class ProcessorFieldSwapper implements Processor {
    @Override
    public Message process(Message message) {
        return message.toBuilder()
                .field11(message.getField11())
                .field12(message.getField11())
                .build();
    }
}
