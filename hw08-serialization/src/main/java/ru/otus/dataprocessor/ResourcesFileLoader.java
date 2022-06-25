package ru.otus.dataprocessor;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.otus.dataprocessor.exception.FileProcessException;
import ru.otus.model.Measurement;

import java.io.IOException;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

public class ResourcesFileLoader implements Loader {

    private final ObjectMapper mapper;
    private final String fileName;

    private record MeasurementData(String name, double value) { }

    public ResourcesFileLoader(String fileName) {
        this.fileName = requireNonNull(fileName);
        mapper = new ObjectMapper();
    }

    @Override
    public List<Measurement> load() {
        try (var is = getClass().getClassLoader().getResourceAsStream(fileName);
             MappingIterator<MeasurementData> iterator = mapper.readerFor(MeasurementData.class).readValues(is)) {

            return iterator.readAll()
                    .stream()
                    .map(data -> new Measurement(data.name(), data.value()))
                    .collect(toList());

        } catch (IOException e) {
            throw new FileProcessException(e);
        }
    }
}
