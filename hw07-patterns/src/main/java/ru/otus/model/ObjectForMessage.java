package ru.otus.model;

import java.util.List;

import static java.util.List.copyOf;

public class ObjectForMessage implements Cloneable {
    private List<String> data;

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
        this.data = data;
    }

    @Override
    public ObjectForMessage clone() {
        var copy = new ObjectForMessage();
        if (this.getData() != null) {
            copy.setData(copyOf(this.getData()));
        }
        return copy;
    }
}