package ru.otus.dataprocessor;

import ru.otus.model.Measurement;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.summingDouble;


public class ProcessorAggregator implements Processor {
    @Override
    public Map<String, Double> process(List<Measurement> data) {
        return new TreeMap<>(
                data.stream().collect(
                    groupingBy(
                        Measurement::getName,
                        summingDouble(Measurement::getValue)
                    )
                )
        );
    }
}