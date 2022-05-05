package ru.otus.dataprocessor;

import com.google.gson.Gson;
import ru.otus.model.Measurement;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class ResourcesFileLoader implements Loader {

    private final File file;

    public ResourcesFileLoader(String fileName) {
        this.file = new File(getClass().getClassLoader().getResource(fileName).getFile());
    }

    @Override
    public List<Measurement> load() throws IOException {
        try{
            Scanner scanner = new Scanner(file);
            String jsonString = scanner.nextLine();
            Gson gson = new Gson();

            return Arrays.asList(gson.fromJson(jsonString, Measurement[].class));
        } catch (Exception e) {
            throw new FileProcessException(e.getMessage());
        }
    }
}
