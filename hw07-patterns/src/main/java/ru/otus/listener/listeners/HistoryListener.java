package ru.otus.listener.listeners;

import ru.otus.listener.Listener;
import ru.otus.model.Message;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static java.util.Optional.ofNullable;


public class HistoryListener implements Listener, HistoryReader {

    private final Map<Long, Message> history;

    public HistoryListener() {
        this.history = new HashMap<>();
    }

    @Override
    public void onUpdated(Message msg) {
        history.put(msg.getId(), msg.clone());
    }

    @Override
    public Optional<Message> findMessageById(long id) {
        return ofNullable(history.get(id));
    }
}
