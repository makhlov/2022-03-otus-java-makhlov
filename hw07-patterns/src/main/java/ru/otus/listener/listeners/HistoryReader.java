package ru.otus.listener.listeners;

import ru.otus.model.Message;

import java.util.Optional;

public interface HistoryReader {
    Optional<Message> findMessageById(long id);
}
