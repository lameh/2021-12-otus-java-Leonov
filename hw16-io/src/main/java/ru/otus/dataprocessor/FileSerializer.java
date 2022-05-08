package ru.otus.dataprocessor;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;
import java.util.stream.Collectors;

public class FileSerializer implements Serializer {

    private final Path pathToFile;
    public FileSerializer(String fileName) {
        this.pathToFile = Paths.get(fileName);
    }
    @Override
    public void serialize(Map<String, Double> data) throws IOException {
        //формирует результирующий json и сохраняет его в файл
        try {
            String json = data.entrySet().stream().map(d -> "\"" + d.getKey() + "\":" + d.getValue())
                    .sorted(String::compareTo).collect(Collectors.joining(",", "{", "}"));
            Files.writeString(pathToFile, json);
        } catch (Exception e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
